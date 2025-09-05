package ru.bsvyazi.bsconnect.Activity

import ru.bsvyazi.bsconnect.utils.PaymentReminderWorker
import UserData
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "ResourceAsColor", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val userData : UserData? = intent.getParcelableExtra("USER_DATA")!!

        //поверяем доступность обещанного платежа если не доступен гасим кнопку
        val creditButton: Button = findViewById(R.id.credit)
        val creditSize = userData?.credit?.toInt()
        // заплатка
        if (creditSize != null) {
            creditButton.isEnabled = false
            binding.creditInfo.text = "Обещанный платеж: $creditSize руб."
        } else {
            creditButton.isEnabled = true
            binding.creditInfo.text = "Обещанный платеж: не активен"
        }

        //заполняем экран информацией об абоненте

        //получаем инфу о дате оплаты
        var payDate = userData?.date_abonka.toString()
        // если абонет отключен ставим дату платежа - текущую дату
        if (payDate == "") {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            payDate = dateFormat.format(Date()).toString()
        } else {
            // запускаем процесс который его оповестит потом
            //schedulePaymentReminder(payDate)
        }
        binding.payDate.text = "Следующий платеж до $payDate"
        binding.address.text = userData?.address
        val statusTextView: TextView = findViewById(R.id.curstatus)
        if (userData?.state == "1") {
            statusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.ok))
            statusTextView.setTextColor(Color.WHITE)
            binding.curstatus.text = "Активен"
        } else {
            statusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.alert))
            statusTextView.setTextColor(Color.WHITE)
            binding.curstatus.text = "Отключен"
        }
        val currentBalance = userData?.deposit?.toDouble()
        binding.balance.text = "Текущий баланс: " + String.format("%.2f", currentBalance) + " руб."
        binding.tariff.text = "Тариф: " + userData?.tarif_fixed_cost
        val internetPrice = userData?.tarif_fixed_cost?.toDouble()?.toInt()
        binding.internetprice.text = "Абонплата: $internetPrice руб."
        binding.fee.text = "Дополнительно: " + intent.getStringExtra("SUBSCRIPTION")
        val feePrice = intent.getStringExtra("SUBSCRIPTION_PRICE")?.toDouble()?.toUInt().toString()
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
            val intent = Intent(Intent.ACTION_VIEW, basePayUrl.toUri())
            startActivity(intent)
        }
        binding.credit.setOnClickListener {
            val intent = Intent(this@MainActivity, CreditActivity::class.java)
            intent.putExtra("ADDRESS", "_userData.address")
            intent.putExtra("TOTALPRICE", totalPrice.toString())
            startActivity(intent)
        }
    }

    fun schedulePaymentReminder(paymentDate: String) {
        // Обработка даты
        val paymentDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val paymentDateObj = paymentDateFormat.parse(paymentDate) ?: return

        // Получаем текущее время и добавляем 3 дня
        val currentTime = Calendar.getInstance()
        currentTime.time = paymentDateObj
        currentTime.add(Calendar.DAY_OF_YEAR, -1) // Устанавливаем на 3 дня раньше

        // Запланируем уведомление
        val inputData = Data.Builder()
            .putString("payment_date", paymentDate)
            .build()

        val reminderRequest = OneTimeWorkRequest.Builder(PaymentReminderWorker::class.java)
            .setInputData(inputData)
            .setInitialDelay(
                currentTime.timeInMillis - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
            ) // Задержка до времени уведомления
            .build()

        WorkManager.getInstance(this).enqueue(reminderRequest)
    }
}