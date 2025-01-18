package ru.bsvyazi.bsc

import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class Repository {

    public var fio : String = ""
    public var tarif : String = ""
    public var amount : String = ""

    fun getUserInfo(login : String, password : String) {
        val client = OkHttpClient()
        val formBody = FormBody.Builder().add("login", login).add("password", password)
            .build()
        val request = Request.Builder()
            .url("https://api.bsvyazi.ru/api/v1/cabinet/auth/login").post(formBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonObject = JsonParser.parseString(responseData).asJsonObject
                    val apiKey = jsonObject.getAsJsonObject("data").get("token").asString
                    // запрашиваем информацию об абоненте
                    val formBodyUser = FormBody.Builder().add("Autorization", apiKey).build()
                    val userRequest = Request.Builder()
                        .url("https://api.bsvyazi.ru/api/v1/cabinet/user").post(formBodyUser).build()
                    client.newCall(userRequest).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {
                            if (response.isSuccessful) {
                                val userResponseData = response.body?.string()
                                val userJsonObject = JsonParser.parseString(userResponseData).asJsonObject
                                fio = userJsonObject.getAsJsonObject("data").get("fio").asString
                                tarif = userJsonObject.getAsJsonObject("data").get("tarif").asString
                                amount = userJsonObject.getAsJsonObject("data").get("abonplata").asString
                            } else {
                                println("User info Request failed with code: ${response.code}")
                            }
                        }
                    })
                } else {
                    println("API Key Request failed with code: ${response.code}")
                }
            }
        })
    }
}