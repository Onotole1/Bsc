package ru.bsvyazi.bsconnect.utils

import java.text.SimpleDateFormat
import java.util.*

fun changeDayInDate(dateString: String, dayChange: Int): String? {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, dayChange)
        dateFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


