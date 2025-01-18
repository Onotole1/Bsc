package ru.bsvyazi.bsc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.bsvyazi.bsc.databinding.ActivityLoginBinding
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.autorization.setOnClickListener {
            if (binding.login.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                binding.message.text = getString(R.string.error_empty_content)
            } else {
                binding.message.text = getString(R.string.autorization_message)
                val url = "https://api.bsvyazi.ru/api/v1/cabinet/auth/login"
//                val login = binding.login.text.toString()
//                val password = binding.password.text.toString()
                val login = "cher0059"
                val password = "2056"
                val client = OkHttpClient()
                val formBody = FormBody.Builder()
                    .add("login", login)
                    .add("password", password)
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        binding.message.text = getString(R.string.autorization_error)
                    }
                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val responseData = response.body?.string()
                            val jsonObject = JsonParser.parseString(responseData).asJsonObject
                            val apiKey = jsonObject.getAsJsonObject("data").get("token").asString
                            println(apiKey)
                        } else {
                            println("Request failed with code: ${response.code}")
                        }
                    }
                })
            }
            finish()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}