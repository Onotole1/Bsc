package ru.bsvyazi.bsconnect

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.bsvyazi.bsconnect.Repository._userData
import ru.bsvyazi.bsconnect.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        //поверяем доступность обещанного платежа если не доступен гасим кнопку
        val creditButton: Button = findViewById(R.id.credit)
        val creditSize = _userData.credit.toInt()
        if (creditSize > 0) {
            creditButton.isEnabled = false
            binding.creditInfo.text = "Обещанный платеж: $creditSize руб."
        } else {
            creditButton.isEnabled = true
            binding.creditInfo.text = "Обещанный платеж: не активен"
        }

        //заполняем экран информацией об абоненте

        var payDate = _userData.endDate
        // если абонет отключен ставим дату платежа - текущую дату
        if (payDate == "") {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            payDate = dateFormat.format(Date()).toString()
        }
        binding.payDate.text = "Следующий платеж до $payDate"
        binding.address.text = intent.getStringExtra("ADDRESS")
        val statusTextView: TextView = findViewById(R.id.curstatus)
        if (intent.getStringExtra("STATUS") == "0") {
            statusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.ok))
            statusTextView.setTextColor(Color.WHITE)
            binding.curstatus.text = "Активен"
        } else {
            statusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.alert))
            statusTextView.setTextColor(Color.WHITE)
            binding.curstatus.text = "Отключен"
        }
        val currentBalance = intent.getStringExtra("BALANCE")?.toDouble()
        binding.balance.text = "Текущий баланс: " + String.format("%.2f", currentBalance) + " руб."
        binding.tariff.text = "Тариф: " + intent.getStringExtra("TARIF")
        val internetPrice = intent.getStringExtra("INTERNETPRICE")?.toDouble()?.toInt()
        binding.internetprice.text = "Абонплата: $internetPrice руб."
        binding.fee.text = "Дополнительно: " + intent.getStringExtra("FEE")
        val feePrice = intent.getStringExtra("FEEPRICE")?.toDouble()?.toInt()
        binding.feeprice.text = "Абонплата: $feePrice руб."
        val totalPrice = feePrice?.plus(internetPrice!!)
        binding.totalprice.text = "Итого:     $totalPrice руб."
        val personalAccount = intent.getStringExtra("LOGIN")
//        binding.site.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bsvyazi.ru/"))
//            startActivity(intent)
//        }
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/bsvyazi"))
//            startActivity(intent)
//        }
        binding.pay.setOnClickListener {
            val basePayUrl =
                "https://payframe.ckassa.ru/?service=17014-17197-1&Л_СЧЕТ=$personalAccount"
            val intent1 = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent1)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(basePayUrl))
            startActivity(intent)
        }
        binding.credit.setOnClickListener {
            val intent = Intent(this@MainActivity, CreditActivity::class.java)
            intent.putExtra("ADDRESS", _userData.address)
            intent.putExtra("TOTALPRICE", totalPrice.toString())
            startActivity(intent)
        }
    }
}