package ru.bsvyazi.bsconnect

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ru.bsvyazi.bsconnect.Repository._error
import ru.bsvyazi.bsconnect.Repository._errorCode
import ru.bsvyazi.bsconnect.Repository._userData
import ru.bsvyazi.bsconnect.Repository.getData
import ru.bsvyazi.bsconnect.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.message.text = ""

        // проверяем наличие интернета
        if (!isInternetAvailable(this)) {
            val intent = Intent(this@LoginActivity, AccessActivity::class.java)
            startActivity(intent)
        }

        // проверяем наличие файла с данными для входа в приложение
        if (isFileExists(this)) {
            readFromFile(this)
            val loginEditText: EditText = findViewById(R.id.login)
            loginEditText.setText(login)
            val passwordEditText: EditText = findViewById(R.id.password)
            passwordEditText.setText(password)
        }
        val myTextView: TextView = findViewById(R.id.message)
        val myCheckBox: CheckBox = findViewById(R.id.saveCheckBox)
        myCheckBox.isChecked = true

        binding.autorization.setOnClickListener {
            myTextView.setTextColor(Color.BLACK)
            if (binding.login.text.isNullOrBlank() || binding.password.text.isNullOrBlank()) {
                myTextView.setTextColor(ContextCompat.getColor(this, R.color.alert))
                binding.message.text = "Пустой пароль или логин"
            } else {
                binding.message.text = "Авторизация.."
                getData(binding.login.text.toString(), binding.password.text.toString())
                //println(_userData)
                Thread.sleep(2000)
                if (_userData.address !== "") {
                    // проверка чекбокса сохранять/не сохранять
                    if (binding.saveCheckBox.isChecked) {
                        writeToFile(
                            this,
                            binding.login.text.toString(),
                            binding.password.text.toString()
                        )
                    } else deleteFile(this)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("ADDRESS", _userData.address)
                    intent.putExtra("TARIF", _userData.tarif)
                    intent.putExtra("BALANCE", _userData.amount)
                    intent.putExtra("LOGIN", _userData.login)
                    intent.putExtra("STATUS", _userData.status)
                    intent.putExtra("FEE", _userData.feeName)
                    intent.putExtra("FEEPRICE", _userData.feePrice)
                    intent.putExtra("INTERNETPRICE", _userData.internetPrice)
                    startActivity(intent)
                } else {
                    myTextView.setTextColor(ContextCompat.getColor(this, R.color.alert))
                    if (_errorCode == 0 && _error == "") {
                        binding.message.text = "Ошибка получения данных"
                    } else {
                        if (_error == "") {
                            binding.message.text = "Ошибка авторизации $_errorCode"
                        }
                        if (_errorCode == 0) {
                            binding.message.text = "Ошибка авторизации " + _error
                        }
                    }
                }
            }
        }
    }
}
