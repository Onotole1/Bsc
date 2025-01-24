package ru.bsvyazi.bsc

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import ru.bsvyazi.bsc.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (isFileExists(this)) {
            readFromFile(this)
            val loginEditText : EditText = findViewById(R.id.login)
            loginEditText.setText(login)
            val passwordEditText : EditText = findViewById(R.id.password)
            passwordEditText.setText(password)
        }
        val myTextView : TextView = findViewById(R.id.message)
        binding.autorization.setOnClickListener {
            myTextView.setTextColor(Color.BLACK)
            if (binding.login.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                myTextView.setTextColor(Color.RED)
                binding.message.text = "Ошибка авторизации"
            } else {
                binding.message.text = "Авторизация.."
                getUserInfo(binding.login.text.toString(), binding.password.text.toString())
                if (fio !== "") {
                    if (binding.saveCheckBox.isChecked) {
                        writeToFile(this,binding.login.text.toString(), binding.password.text.toString())
                    }
                    //finish()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("FIO", fio)
                    intent.putExtra("TARIF", tarif)
                    intent.putExtra("AMOUNT", amount)
                    intent.putExtra("LOGIN", login)
                    startActivity(intent)
                } else {
                    myTextView.setTextColor(Color.RED)
                    binding.message.text = "Ошибка авторизации"
                }
            }
        }
    }
}