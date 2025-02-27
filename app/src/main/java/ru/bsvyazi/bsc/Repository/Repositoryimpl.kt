import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.JsonElement
import ru.bsvyazi.bsc.Repository.UserData

val client = OkHttpClient()
var apiKey: String = ""
val userAgent = "MikbillApiAgent/1.0"
var _userData = UserData()
var _errorCode: Int = 0
var _error: String = ""

data class Fee (
    val id: String,
    val name: String,
    val price: Int,
    val discount: Int,
    val price_with_discount: Int
)

fun extractFee(data: JsonElement) {
    if (data.toString().equals("[]")) return
    val gson = Gson()
    val FeeListType = object : TypeToken<List<Fee>>() {}.type
    val feeList: List<Fee> = gson.fromJson(data, FeeListType)
    _userData.feeName = feeList.get(0).name
    _userData.feePrice = feeList.get(0).price.toString()
    println()
}

fun getData(login: String, password: String) {
    val url = "https://api.bsvyazi.ru/api/v1/cabinet/auth/login"
    val requestBody = FormBody.Builder()
        .add("login", login)
        .add("password", password)
        .build()
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .addHeader("User-Agent", userAgent)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            _error = e.message.toString()
            //println("Apikey Request failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (it.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)
                    apiKey = jsonObject.getJSONObject("data").getString("token")
                    //println("API Key: $apiKey")
                    fetchData()
                } else {
                    // println("Apikey Request failed with code: ${it.code}")
                    _errorCode = it.code
                }
            }
        }
    })
}

fun fetchData() {
    val request = Request.Builder()
        .url("https://api.bsvyazi.ru/api/v1/cabinet/user")
        .addHeader("User-Agent", userAgent)
        .addHeader("Authorization", apiKey)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            _error = e.message.toString()
            //println("Data Request failed: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (it.isSuccessful) {
                    val userData = it.body?.string()
                    val userJsonObject =
                        JsonParser.parseString(userData).asJsonObject
                    _userData.address = userJsonObject.getAsJsonObject("data").get("address").asString
                    _userData.tarif = userJsonObject.getAsJsonObject("data").get("tarif").asString
                    _userData.amount = userJsonObject.getAsJsonObject("data").get("deposit").asString
                    _userData.status = userJsonObject.getAsJsonObject("data").get("blocked").asString
                    _userData.internetPrice= userJsonObject.getAsJsonObject("data").get("tarif_fixed_cost").asString

                    val c = userJsonObject.getAsJsonObject("data").getAsJsonObject("fee")
                        .getAsJsonObject("subscriptions")
                        .get("detailed")
                    extractFee(c)
                    //println("Data: $_userData")
                } else {
                    // println("Data Request failed with code: ${it.code}")
                    _errorCode = it.code
                }
            }
        }
    })
}