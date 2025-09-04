package ru.bsvyazi.bsconnect.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.databinding.ActivityBlagoBinding

class BlagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBlagoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val myTextView: TextView = findViewById(R.id.message)
        if (true) {
            binding.message.text = "ОБЕЩАННЫЙ ПЛАТЕЖ подключен !"
        } else {
            myTextView.setTextColor(Color.RED)
            binding.message.text = "Ошибка подключения ОБЕЩАННЫЙ ПЛАТЕЖ не подключен..."
        }
        binding.ret.setOnClickListener {
            val intent = Intent(this@BlagoActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}