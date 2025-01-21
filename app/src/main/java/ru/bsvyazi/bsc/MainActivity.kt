package ru.bsvyazi.bsc

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.bsvyazi.bsc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val binding = ActivityMainBinding.inflate(layoutInflater)
        binding.clientInfo.text = intent.getStringExtra("FIO")
        binding.tariff.text = intent.getStringExtra("TARIF")
        binding.clientBalance.text = "Баланс : " + intent.getStringExtra("AMOUNT") + "руб."
        val personalAccount = intent.getStringExtra("LOGIN")
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
            val basePayUrl =
                "https://payframe.ckassa.ru/?service=17014-17197-1&Л_СЧЕТ=$personalAccount"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(basePayUrl))
            startActivity(intent)
        }
    }
}