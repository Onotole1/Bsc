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
import ru.bsvyazi.bsconnect.utils.isFileExists
import ru.bsvyazi.bsconnect.utils.isInternetAvailable
import ru.bsvyazi.bsconnect.utils.login
import ru.bsvyazi.bsconnect.utils.password
import ru.bsvyazi.bsconnect.utils.readFromFile
import ru.bsvyazi.bsconnect.utils.writeToFile


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

        // проверяем статус интернет соеденения
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
                setMessage(false, R.string.emptyLoginOrPasswordMessage.toString())
            } else {
                val apiClient = ApiClient()
                var token: String?
                lifecycleScope.launch {
                    token = try {
                        apiClient.loginSuspend(editedLogin.text.toString(), editedPassword.text.toString())
                    } catch (e: Exception) {
                        setMessage(false, R.string.ApiRequestFail.toString())
                        null
                    }
                    if (token == null) {
                        setMessage(false, R.string.BadLoginOrPassword.toString())
                    }
                    setMessage(true, R.string.LoadingData.toString())
                    if (token != null) {
                        val userData = try {
                            apiClient.getUserSuspend(token!!)
                        } catch (e: Exception) {
                            setMessage(false, R.string.DataTransferError.toString())
                            null
                        }
                        if (userData == null) setMessage(false, R.string.BadData.toString())
                        else {
                            // проверка чекбокса сохранять/не сохранять
                            if (saveCheckBox.isChecked) {
                                writeToFile(this@LoginActivity,
                                    editedLogin.text.toString(),
                                    editedPassword.text.toString()
                                )
                            } else ru.bsvyazi.bsconnect.utils.deleteFile(this@LoginActivity)
                            // загружаем данные подписок
                            val service = try {
                                apiClient.getSubscriptionsSuspend(token!!)
                            } catch (e: Exception) {
                                setMessage(false, R.string.SubscriptionDataError.toString())
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

