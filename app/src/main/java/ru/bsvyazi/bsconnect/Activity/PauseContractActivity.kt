package ru.bsvyazi.bsconnect.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import ru.bsvyazi.bsconnect.databinding.ActivityPauseContractBinding

class PauseContractActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPauseContractBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.retry.setOnClickListener {
            val intent = Intent(this@PauseContractActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.phoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:+78004441109".toUri()
            }
            startActivity(intent)

        }
        binding.vkImage.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://vk.com/bsvyazi".toUri())
            startActivity(intent)
        }
    }
}