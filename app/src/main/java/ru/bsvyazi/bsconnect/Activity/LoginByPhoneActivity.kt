package ru.bsvyazi.bsconnect.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.databinding.ActivityLoginBinding
import ru.bsvyazi.bsconnect.databinding.ActivityLoginByPhoneBinding

class LoginByPhoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginByPhoneBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.messageLogin.setOnClickListener{
            val intent = Intent(this@LoginByPhoneActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.vkImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                "https://vk.com/bsvyazi".toUri())
            startActivity(intent)
        }
    }
}