package ru.bsvyazi.bsconnect.utils

import DataForLogin
import android.content.Context
import java.io.File

private val fileName = "credentials.ktx"

fun readFromFile(context: Context) : DataForLogin? {
    try {
        val file = File(context.filesDir, fileName)
        val bufferedReader = file.bufferedReader()
        val lines = bufferedReader.readLines()
        if (lines.size >= 2) return DataForLogin(lines[0],lines[1])
        else return null
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun deleteFile(context: Context) {
    val file = File(context.filesDir, fileName)
    file.delete()
}

fun writeToFile(context: Context, login: String, password: String) {
    try {
        val file = File(context.filesDir, fileName)
        file.bufferedWriter().use { writer ->
            writer.write(login)
            writer.newLine()
            writer.write(password)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}