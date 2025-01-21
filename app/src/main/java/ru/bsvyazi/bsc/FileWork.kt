package ru.bsvyazi.bsc

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private val fileName = "credentials.ktx"
val file = File(fileName)
//var login: String = ""
//var password: String = ""



fun dataFileExist(): Boolean {
    if (file.exists()) return true
    else return false
}

fun loadUserData() {
    if (file.exists()) {
        try {
            val lines = file.readLines()
            if (lines.size >= 2) {
                login = lines[0]
                password = lines[1]
            }
        } catch (e: IOException) {
            println("Ошибка при чтении файла: ${e.message}")
        }
    }
}

fun saveUserData(login: String, password: String) {
    if (!dataFileExist()) {
        try {
            // Create the file if it doesn't exist
            if (file.createNewFile()) {
                println("File created: ${file.name}")
            } else {
                println("File already exists.")
            }
        } catch (e: IOException) {
            // Handle exceptions related to file operations
            println("An error occurred while creating the file.")
            e.printStackTrace()
        }
    }
    try {
        file.writeText("$login\n$password")
    } catch (e: IOException) {
        println("Ошибка при записи в файл: ${e.message}")
    }
}
