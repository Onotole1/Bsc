package ru.bsvyazi.bsconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import ru.bsvyazi.bsconnect.Repository._creditSuccess
import ru.bsvyazi.bsconnect.Repository._userData
import ru.bsvyazi.bsconnect.Repository.getCredit
import ru.bsvyazi.bsconnect.databinding.ActivityCreditBinding

class CreditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val binding = ActivityCreditBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        // если абонет согласен с условиями - активируем кнопку, если нет - деактивируем
        val payButton: Button = findViewById(R.id.pay)
        payButton.isEnabled = false
        binding.confirm.setOnClickListener {
            if (binding.confirm.isChecked) payButton.isEnabled = true
            else payButton.isEnabled = false
        }

        val totalPrice = intent.getStringExtra("TOTALPRICE")
        binding.creditInfo.text =
            binding.creditInfo.text.toString() + " в размере  " + totalPrice + " руб."
        binding.address.text = "Ваш адрес: " + intent.getStringExtra("ADDRESS")
        binding.pay.setOnClickListener {
            getCredit()
            Thread.sleep(2000)
            val intent = Intent(this@CreditActivity, BlagoActivity::class.java)
            startActivity(intent)
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}