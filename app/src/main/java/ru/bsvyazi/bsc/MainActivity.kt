package ru.bsvyazi.bsc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.bsvyazi.bsc.R
import ru.bsvyazi.bsc.databinding.ActivityLoginBinding
import ru.bsvyazi.bsc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.site.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bsvyazi.ru/"))
            startActivity(intent)
        }
        binding.vk.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/bsvyazi"))
            startActivity(intent)
        }
        binding.pay.setOnClickListener {
            val personalAccount = "cher1481"
            val amount = 500
            val basePayUrl = "https://payframe.ckassa.ru/?service=17014-17197-1&Л_СЧЕТ=$personalAccount&amount=123"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(basePayUrl))
            startActivity(intent)
        }
    }
}