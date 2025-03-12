package ru.bsvyazi.bsconnect.Repository

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

val client = OkHttpClient()
var apiKey: String = ""
val userAgent = "MikbillApiAgent/1.0"
var _userData = UserData()
var _errorCode: Int = 0
var _error: String = ""
var _token: String = ""
var _creditSuccess: Boolean = false
var _creditAvailable: Boolean = false
var _creditActive: Boolean = false
var _dataStop: String = ""

data class Fee(
    val id: String,
    val name: String,
    val price: Int,
    val discount: Int,
    val price_with_discount: Int
)

fun getCredit() {
    data class CreditResponse(
        val success: Boolean,
        val code: Int,
        val data: List<Any>,
        val message: String
    )
    val mediaType = "text/plain".toMediaType()
    val JSON = "application/json; charset=utf-8".toMediaType()
    val body = RequestBody.create(JSON, "{}")
    val request = Request.Builder()
        .url("https://api.bsvyazi.ru/api/v1/cabinet/user/services/credit")
        .post(body)
        .addHeader("User-Agent", userAgent)
        .addHeader("Authorization", apiKey)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            _error = e.message.toString()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (it.isSuccessful) {
                    val responseBody = response.body?.string()
                    val gson = Gson()
                    val apiResponse: CreditResponse = gson.fromJson(responseBody, CreditResponse::class.java)
                    _creditSuccess = apiResponse.success
                    println()
                } else {
                    _errorCode = it.code
                }
            }
        }
    })
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
                    try {
                        apiKey = jsonObject.getJSONObject("data").getString("token")
                        _token = apiKey
                    }
                    catch (e: JSONException) {
                        _error = " неверный логин или пароль"
                    }
                    catch (e: Exception) {
                        _error = e.message!!
                    }
                    if (_error == "") {
                        fetchData()
                        _userData.login = login
                    }
                } else {
                    // println("Apikey Request failed with code: ${it.code}")
                    _errorCode = it.code
                }
            }
        }
    })
}

fun extractFee(data: JsonElement) {
    if (data.toString().equals("[]")) return
    val gson = Gson()
    val FeeListType = object : TypeToken<List<Fee>>() {}.type
    val feeList: List<Fee> = gson.fromJson(data, FeeListType)
    _userData.feeName = feeList.get(0).name
    _userData.feePrice = feeList.get(0).price.toString()
    //println()
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
                    _userData.address =
                        userJsonObject.getAsJsonObject("data").get("address").asString
                    _userData.tarif = userJsonObject.getAsJsonObject("data").get("tarif").asString
                    _userData.amount =
                        userJsonObject.getAsJsonObject("data").get("deposit").asString
                    _userData.credit =
                        userJsonObject.getAsJsonObject("data").get("credit").asString
                    _userData.status =
                        userJsonObject.getAsJsonObject("data").get("blocked").asString
                    _userData.endDate =
                        userJsonObject.getAsJsonObject("data").get("date_itog").asString
                    _userData.internetPrice =
                        userJsonObject.getAsJsonObject("data").get("tarif_fixed_cost").asString


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