package ru.bsvyazi.bsconnect

import ApiClient
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.bsvyazi.bsconnect.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val messageTextView: TextView = findViewById(R.id.message)
        messageTextView.text = ""
        val editedLogin: EditText = findViewById(R.id.login)
        val editedPassword: EditText = findViewById(R.id.password)
        val saveCheckBox: CheckBox = findViewById(R.id.saveCheckBox)
        // по умолчанию чекбокс в положение - "сохранять"
        saveCheckBox.isChecked = true

        fun setMessage(status: Boolean, message: String) {
            // установка цвета сообщения BLACK - normal, alert - error
            if (status) messageTextView.setTextColor(Color.BLACK)
            else messageTextView.setTextColor(ContextCompat.getColor(this, R.color.alert))
            messageTextView.text = message
        }

        // проверяем наличие интернет соеденения
        if (!isInternetAvailable(this)) {
            val intent = Intent(this@LoginActivity, AccessActivity::class.java)
            startActivity(intent)
        }

        // проверяем наличие файла с данными для входа
        if (isFileExists(this)) {
            readFromFile(this)
            editedLogin.setText(login)
            editedPassword.setText(password)
        }

        binding.autorization.setOnClickListener {
            if (editedLogin.text.isNullOrBlank() || editedPassword.text.isNullOrBlank()) {
                setMessage(false, "Пустой пароль или логин")
            } else {
                val apiClient = ApiClient()
                var token: String?
                lifecycleScope.launch {
//                    val a = apiClient.phoneSuspend("89135127297")
//                    println(a)
                    token = try {
                        apiClient.loginSuspend(editedLogin.text.toString(), editedPassword.text.toString())
                    } catch (e: Exception) {
                        setMessage(false, "Ошибка запроса")
                        null
                    }
                    if (token == null) {
                        setMessage(false, "Неверный логин или пароль")
                    }
                    setMessage(true, "Получение данных")
                    if (token != null) {
                        val userData = try {
                            apiClient.getUserSuspend(token!!)
                        } catch (e: Exception) {
                            setMessage(false, "Ошибка получения данных")
                            null
                        }
                        if (userData == null) setMessage(false, "Ошибка данных")
                        else {
                            // проверка чекбокса сохранять/не сохранять
                            if (saveCheckBox.isChecked) {
                                writeToFile(this@LoginActivity,
                                    editedLogin.text.toString(),
                                    editedPassword.text.toString()
                                )
                            } else deleteFile(this@LoginActivity)
                            // загружаем данные подписок
                            val service = try {
                                apiClient.getSubscriptionsSuspend(token!!)
                            } catch (e: Exception) {
                                setMessage(false, "Ошибка получения данных подписки")
                                null
                            }

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("USER_DATA", userData)
                            intent.putExtra("LOGIN", editedLogin.text)
                            // проверяем подписки, если нет то передаем нулевые значения
                            if (service != null) {
                                val activeService = service.firstOrNull { it.active > 0 }
                                intent.putExtra("SUBSCRIPTION", activeService?.name)
                                intent.putExtra("SUBSCRIPTION_PRICE", activeService?.info?.service_price)
                            }
                            else {
                                intent.putExtra("SUBSCRIPTION", "")
                                intent.putExtra("SUBSCRIPTION_PRICE", "0")
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

