package ru.bsvyazi.bsconnect.utils

import android.content.Context
import java.io.File

private val fileName = "credentials.ktx"
var login: String = ""
var password: String = ""

fun createFile(context: Context, login: String, password: String) {
    writeToFile(context, login, password)
}

fun isFileExists(context: Context): Boolean {
    val file = File(context.filesDir, fileName)
    return file.exists()
}

fun readFromFile(context: Context) {
    try {
        val file = File(context.filesDir, fileName)
        val bufferedReader = file.bufferedReader()
        val lines = bufferedReader.readLines()
        if (lines.size >= 2) {
            login = lines[0]
            password = lines[1]
        } else {
            println("неправильный файл с данными")
        }
    } catch (e: Exception) {
        e.printStackTrace()
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