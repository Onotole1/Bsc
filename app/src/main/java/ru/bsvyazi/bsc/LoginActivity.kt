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
                getUserInfo(binding.login.text.toString(),binding.password.text.toString())
            }
            finish()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}