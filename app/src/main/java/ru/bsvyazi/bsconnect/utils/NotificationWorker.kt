package ru.bsvyazi.bsconnect.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.work.Logger
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.R.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun checkScheduledNotification(context: Context): Boolean {
    val workInfos = WorkManager.getInstance(context)
        .getWorkInfosByTag(context.getString(string.notificationTag)).get()
    println(workInfos)
    return workInfos.any { info ->
        when (info.state) {
            WorkInfo.State.ENQUEUED,
            WorkInfo.State.RUNNING,
            WorkInfo.State.BLOCKED -> true
            else -> false
        }
    }
}

// Worker для отправки уведомления
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        sendNotification(applicationContext.getString(R.string.notificationTitle),
            applicationContext.getString(R.string.notificationText))
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений (для Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Напоминания",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(drawable.bs_logo)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}

// Функция для планирования уведомления
fun scheduleNotification(context: Context, dateString: String) {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val targetDate: Date? = try {
        sdf.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    targetDate?.let {
        val currentTime = System.currentTimeMillis()
        val delay = it.time - currentTime
        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(context.getString(string.notificationTag))
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}