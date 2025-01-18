package ru.bsvyazi.bsc.service

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class BillRequests {

    val URL = "https://api.bsvyazi.ru/api/v1/cabinet/auth/login"
    private val client = OkHttpClient()

    fun getApiKey(login: String, password: String)  {
        val request = Request.Builder()
            .url(URL)
            .header(login,password)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = println(response?.toString())
        })
    }
}