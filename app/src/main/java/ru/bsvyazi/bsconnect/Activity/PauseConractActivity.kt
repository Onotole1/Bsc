package ru.bsvyazi.bsconnect.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import ru.bsvyazi.bsconnect.databinding.ActivityPauseConractBinding

class PauseConractActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPauseConractBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
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