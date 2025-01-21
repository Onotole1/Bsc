package ru.bsvyazi.bsc

import java.util.concurrent.TimeUnit
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

var fio: String = ""
var tarif: String = ""
var amount: String = ""

fun getUserInfo(login: String, password: String) {
    val client = OkHttpClient.Builder()
        .callTimeout(20, TimeUnit.SECONDS)
        .build()
    val formBody = FormBody.Builder().add("login", login).add("password", password)
        .build()
    val request = Request.Builder()
        .url("https://api.bsvyazi.ru/api/v1/cabinet/auth/login")
        .post(formBody).build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println(e)
        }
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                val jsonObject = JsonParser.parseString(responseData).asJsonObject
                val apiKey = jsonObject.getAsJsonObject("data").get("token").asString
                // запрашиваем информацию об абоненте
                val client2 = OkHttpClient.Builder()
                    .callTimeout(20, TimeUnit.SECONDS)
                    .build()
                val userRequest = Request.Builder()
                    .url("https://api.bsvyazi.ru/api/v1/cabinet/user")
                    .addHeader("Authorization", apiKey)
                    .build()
                client2.newCall(userRequest).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val userResponseData = response.body?.string()
                            val userJsonObject =
                                JsonParser.parseString(userResponseData).asJsonObject
                            fio = userJsonObject.getAsJsonObject("data").get("fio").asString
                            tarif = userJsonObject.getAsJsonObject("data").get("tarif").asString
                            amount =
                                userJsonObject.getAsJsonObject("data").get("deposit").asString
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
