package ru.bsvyazi.bsconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.bsvyazi.bsconnect.databinding.ActivityAccessBinding

class AccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAccessBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.retry.setOnClickListener {
            val intent = Intent(this@AccessActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}