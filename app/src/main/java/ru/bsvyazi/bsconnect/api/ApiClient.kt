package ru.bsvyazi.bsconnect.api

import ApiService
import Service
import UserAgentInterceptor
import UserData
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor() {
    private val userAgent: String = "MikbillApiAgent/1.0"
    private val apiService: ApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("ApiServiceLog", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor(userAgent))
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.bsvyazi.ru/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun loginSuspend(login: String, password: String): String? {
        Log.d("ApiClient", "Login $login Password $password")
        val response = apiService.login(login, password)
        return if (response != null && response.success && response.data != null) {
            Log.d("ApiClient", "Login successful, token: ${response.data.token}")
            response.data.token.trim()
        } else {
            Log.w("ApiClient", "Login failed or response null")
            null
        }
    }

    suspend fun phoneSuspend(phone: String): Boolean {
        try {
            val mediaType = "text/plain".toMediaTypeOrNull()
            val phoneBody = RequestBody.create(mediaType, phone)
            val response = apiService.phone(phoneBody)
            Log.d("ApiClient", "Phone successful")
            return true
        } catch(e: Exception)
        {
            Log.d("ApiClient", "Phone not found ??? $e")
            return false
        }
    }

    suspend fun getUserSuspend(apiKey: String): UserData? {
        val response = apiService.getUser(apiKey)
        return if (response != null && response.success && response.data != null) {
            Log.d("ApiClient", "User data fetched successfully")
            response.data
        } else {
            Log.w("ApiClient", "User fetch failed or response null")
            null
        }
    }

    suspend fun getSubscriptionsSuspend(apiKey: String): List<Service>? {
        val response = apiService.getSubscriptions(apiKey)
        return if (response != null && response.success && response.data != null) {
            Log.d("ApiClient", "User data fetched successfully")
            response.data
        } else {
            Log.w("ApiClient", "Subscriptions data fetch failed or response null $response.success ")
            null
        }
    }

    suspend fun creditActivationSuspend(apiKey: String) {
        val response = apiService.creditActivation(apiKey)
        if (response != null && response.success) {
            Log.d("ApiClient", "credit is active")
        } else {
            Log.w("ApiClient", "credit activate fail")
        }
    }
}