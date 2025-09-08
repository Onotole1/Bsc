package ru.bsvyazi.bsconnect.Activity

import ru.bsvyazi.bsconnect.utils.PaymentReminderWorker
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
import java.time.YearMonth


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
        binding.balance.text = "Текущий баланс: " + String.format("%.2f", currentBalance) + " руб."
        binding.tariff.text ="Тарифный план: " + removeBracket(userData?.tarif)
        val price = userData?.tarif_fixed_cost?.toDoubleOrNull()
        binding.abonplata.text = "Цена: " + String.format("%.2f", price) + " руб."
        var total : Double
        val tvLogo: ImageView = findViewById(R.id.tv_logo)
        if (subscription != null) {
            // выводим логотип ТВ провайдера
            if (subscription.contains("moovi")) tvLogo.setImageResource(R.drawable.moovi_logo)
            else tvLogo.setImageResource(R.drawable.smotreshka_logo)

            binding.fee.text = "Доп.: " + subscription.replace(Regex("\\[.*?\\]"), "")
            val sub_price = subscription_price?.toDoubleOrNull()
            binding.feePrice.text = "Цена: " + String.format("%.2f", sub_price) + " руб."
            total = sub_price?.let { price?.plus(it) }!!
            binding.total.text = "Итого: " + String.format("%.2f", total) + " руб."
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
        if ((total/getCurrentMonthDays()) < currentBalance!!) {
            offPicture.isVisible = false
            binding.payDate.text = "Следующий платеж до $payDate"
        }
        else {
            offPicture.isVisible = true
            //binding.payDate.text = "Статус:"
        }

        //поверяем доступность обещанного платежа если не доступен гасим кнопку
        val creditButton: Button = findViewById(R.id.credit)
        if (userData.credit != "0") creditButton.isEnabled = false
        else creditButton.isEnabled = true




        //получаем инфу о дате оплаты

        // если абонет отключен ставим дату платежа - текущую дату
        if (payDate == "") {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            payDate = dateFormat.format(Date()).toString()
        } else {
            // запускаем процесс который его оповестит потом
            //schedulePaymentReminder(payDate)
        }

//        val statusTextView: TextView = findViewById(R.id.curstatus)
//        if (userData?.state == "1") {
//            statusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.ok))
//            statusTextView.setTextColor(Color.WHITE)
//            binding.curstatus.text = "Активен"
//        } else {
//            statusTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.alert))
//            statusTextView.setTextColor(Color.WHITE)
//            binding.curstatus.text = "Отключен"
//        }
//
//
//        val internetPrice = userData?.tarif_fixed_cost?.toDouble()?.toInt()
//        binding.internetprice.text = "Абонплата: $internetPrice руб."
//        binding.fee.text = "Дополнительно: " + intent.getStringExtra("SUBSCRIPTION")
//        val feePrice = intent.getStringExtra("SUBSCRIPTION_PRICE")?.toDouble()?.toUInt().toString()
//        binding.feeprice.text = "Абонплата: $feePrice руб."
//        val totalPrice = feePrice?.plus(internetPrice!!)
//        binding.totalprice.text = "Итого:     $totalPrice руб."


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