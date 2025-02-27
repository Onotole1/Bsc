package ru.bsvyazi.bsc

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.bsvyazi.bsc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.address.text = intent.getStringExtra("ADDRESS")
        val statusTextView: TextView = findViewById(R.id.curstatus)
        if (intent.getStringExtra("STATUS") == "0") {
            statusTextView.setBackgroundColor(Color.GREEN)
            binding.curstatus.text = "Активен"
        }
        else {
            statusTextView.setBackgroundColor(Color.RED)
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
        if (feePrice !== 0) {

        }
        binding.feeprice.text =   "Абонплата: $feePrice руб."
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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(basePayUrl))
            startActivity(intent)
        }
    }
}