import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class PaymentReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val paymentDateString = inputData.getString("payment_date") ?: return Result.failure()
        val paymentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(paymentDateString)

        // Получаем текущую дату
        val currentDate = Calendar.getInstance().time

        // Проверяем, что до даты оплаты осталось 3 дня
        val differenceInMillis = paymentDate.time - currentDate.time
        val daysUntilPayment = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

        // Отправляем уведомление, если 3 дня до оплаты
        //if (daysUntilPayment == 1) {
            showNotification("Напоминание!", "У вас осталось 3 дня до срока оплаты.")
        //}

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1 // Уникальный идентификатор уведомления

        // Создание канала уведомлений для Android 8.0 и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "payment_notifications"
            val channelName = "Payment Notifications"
            val channelDescription = "Notifications for payment reminders"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
        }

        // Создание уведомления
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "payment_notifications")
            .setContentTitle(title)
            .setContentText(message)
            //.setSmallIcon(R.drawable.bs_pic_background) // Убедитесь, что иконка существует
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}