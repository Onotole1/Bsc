package ru.bsvyazi.bsconnect.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.databinding.ActivityCreditBinding

class CreditActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
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
        binding.creditInfo.text = getString(R.string.credit_info) +
                intent.getStringExtra("TOTALPRICE") + getString(R.string.valute)

        binding.address.text = getString(R.string.userAddressMsg) + intent.getStringExtra("ADDRESS")
        binding.back.setOnClickListener {
            val intent = Intent(this@CreditActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.pay.setOnClickListener {
//            lifecycleScope.launch {
//                token = try {
//                    apiClient.loginSuspend(
//                        editedLogin.text.toString(),
//                        editedPassword.text.toString()
//                    )
//                } catch (e: Exception) {
//                    setMessage(false, R.string.ApiRequestFail)
//                    null
//                }
                val intent = Intent(this@CreditActivity, BlagoActivity::class.java)
                startActivity(intent)
            }
        }
    }
