package ru.bsvyazi.bsconnect.Activity

import UserData
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.core.net.toUri
import androidx.core.view.isVisible
import ru.bsvyazi.bsconnect.utils.scheduleAlarm
import java.time.YearMonth
import android.content.Context
import ru.bsvyazi.bsconnect.utils.scheduleNotification
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "ResourceAsColor", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        fun removeBracket(input: String?): String {
            if (input == null) return ""
            else return input.replace(Regex("\\([^)]*\\)"), "")
        }

        fun getCurrentMonthDays(): Int {
            // Get the current month and year
            val currentMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                YearMonth.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            // Get the number of days in the current month
            val daysInMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentMonth.lengthOfMonth()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            return daysInMonth
        }

        val userData : UserData? = intent.getParcelableExtra("USER_DATA")!!
        val subscription : String? = intent.getStringExtra("SUBSCRIPTION")
        val subscription_price : String? = intent.getStringExtra("SUBSCRIPTION_PRICE")

        //заполняем экран информацией об абоненте
        binding.fio.text = userData?.fio
        val currentBalance = userData?.deposit?.toDouble()
        binding.balance.text = getString(R.string.userBalanceMsg) + " " + String.format("%.2f", currentBalance) + " руб."
        binding.tariff.text = getString(R.string.userServiceNameMsg) + " " + removeBracket(userData?.tarif)
        val price = userData?.tarif_fixed_cost?.toDoubleOrNull()
        binding.abonplata.text = getString(R.string.userServiceCostMsg) + " " + String.format("%.2f", price) + " руб."
        var total : Double
        val tvLogo: ImageView = findViewById(R.id.tv_logo)
        if (subscription != null) {
            // выводим логотип ТВ провайдера
            if (subscription.contains("moovi")) tvLogo.setImageResource(R.drawable.moovi_logo)
            else tvLogo.setImageResource(R.drawable.smotreshka_logo)

            binding.fee.text = getString(R.string.userSubscrNameMsg) + " " + subscription.replace(Regex("\\[.*?\\]"), "")
            val sub_price = subscription_price?.toDoubleOrNull()
            binding.feePrice.text = getString(R.string.userServiceCostMsg) + " " + String.format("%.2f", sub_price) + " руб."
            total = sub_price?.let { price?.plus(it) }!!
            binding.total.text = getString(R.string.userTotalCostMsg) + " " + String.format("%.2f", total) + " руб."
        }
        else {
            // гасим лишние разделители
            tvLogo.isVisible = false
            total = price!!
            binding.line1.isVisible = false
            binding.line2.isVisible = false
            binding.line3.isVisible = false
        }
        var payDate = userData?.date_itog.toString()
        val offPicture: ImageView = findViewById(R.id.off_picture)

        // проверяем, не отключен ли абонент
        if ((total/getCurrentMonthDays()) < currentBalance!!) {
            offPicture.isVisible = false
            binding.payDate.text = getString(R.string.userNextPayDateMsg) + " $payDate"
            //если не отключен планируем напоминание пушем
            scheduleNotification(context, "24-09-2025")
        }
        else {
            offPicture.isVisible = true
        }

        //поверяем доступность обещанного платежа если не доступен гасим кнопку
        val creditButton: Button = findViewById(R.id.credit)
        if (userData.credit != "0") {
            creditButton.isEnabled = false
            offPicture.isVisible = false
            binding.payDate.text = getString(R.string.userNextPayDateMsg) + " $payDate"
        }
        else creditButton.isEnabled = true

        binding.pay.setOnClickListener {
            //login абонента
            val personalAccount = userData.user
            val basePayUrl =
                "https://payframe.ckassa.ru/?service=17014-17197-1&Л_СЧЕТ=$personalAccount"
            val intent = Intent(Intent.ACTION_VIEW, basePayUrl.toUri())
            startActivity(intent)
        }
        binding.credit.setOnClickListener {
            val intent = Intent(this@MainActivity, CreditActivity::class.java)
            intent.putExtra("ADDRESS", "_userData.address")
            intent.putExtra("TOTALPRICE", 100.toString())
            startActivity(intent)
        }
    }
}