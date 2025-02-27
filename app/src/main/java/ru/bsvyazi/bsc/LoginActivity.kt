package ru.bsvyazi.bsc

import _error
import _errorCode
import _userData
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import getData
import ru.bsvyazi.bsc.databinding.ActivityLoginBinding
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.message.text = ""
        if (isFileExists(this)) {
            readFromFile(this)
            val loginEditText: EditText = findViewById(R.id.login)
            loginEditText.setText(login)
            val passwordEditText: EditText = findViewById(R.id.password)
            passwordEditText.setText(password)
        }
        val myTextView: TextView = findViewById(R.id.message)
        binding.autorization.setOnClickListener {
            myTextView.setTextColor(Color.BLACK)
            if (binding.login.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                myTextView.setTextColor(Color.RED)
                binding.message.text = "Пустой пароль или логин"
            } else {
                binding.message.text = "Авторизация.."
                getData(binding.login.text.toString(), binding.password.text.toString())
                //println(_userData)
                Thread.sleep(2000)
                if (_userData.address !== "") {
                    if (binding.saveCheckBox.isChecked) {
                        writeToFile(
                            this,
                            binding.login.text.toString(),
                            binding.password.text.toString()
                        )
                    }
                    else {
                        //ru.bsvyazi.bsc.deleteFile(this)
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("ADDRESS", _userData.address)
                    intent.putExtra("TARIF", _userData.tarif)
                    intent.putExtra("BALANCE", _userData.amount)
                    intent.putExtra("LOGIN", login)
                    intent.putExtra("STATUS", _userData.status)
                    intent.putExtra("FEE", _userData.feeName)
                    intent.putExtra("FEEPRICE", _userData.feePrice)
                    intent.putExtra("INTERNETPRICE", _userData.internetPrice)
                    startActivity(intent)
                } else {
                    myTextView.setTextColor(Color.RED)
                    if (_errorCode == 0 && _error == "") {
                        binding.message.text = "Ошибка получения данных"
                    }
                    else {
                        if (_error == "") {
                            binding.message.text = "Ошибка авторизации $_errorCode"
                        }
                        if (_errorCode == 0) {
                            binding.message.text = "Ошибка авторизации" + _error
                        }
                    }
                }
            }
        }
    }
}