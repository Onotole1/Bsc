package ru.bsvyazi.bsconnect.Activity

import ApiClient
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.databinding.ActivityLoginBinding
import ru.bsvyazi.bsconnect.utils.isInternetAvailable
import ru.bsvyazi.bsconnect.utils.readFromFile
import ru.bsvyazi.bsconnect.utils.writeToFile


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val messageTextView: TextView = findViewById(R.id.message)
        //messageTextView.text = ""
        val editedLogin: EditText = findViewById(R.id.login)
        val editedPassword: EditText = findViewById(R.id.password)
        val saveCheckBox: CheckBox = findViewById(R.id.saveCheckBox)
        // по умолчанию чекбокс в положение - "сохранять"
        saveCheckBox.isChecked = true

        fun setMessage(status: Boolean, linkResMessage: Int) {
            // установка цвета сообщения text_color - normal, alert - error
            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            if (status) messageTextView.setTextColor(getColor(R.color.text_color))
            else messageTextView.setTextColor(ContextCompat.getColor(this, R.color.alert))
            messageTextView.text = getString(linkResMessage)
        }

        // проверяем статус интернет соеденения
        if (!isInternetAvailable(this)) {
            val intent = Intent(this@LoginActivity, AccessActivity::class.java)
            startActivity(intent)
        }

        // проверяем наличие файла с данными для входа
        val dataForLogin = readFromFile(this)
        if (dataForLogin !== null) {
            editedLogin.setText(dataForLogin.login)
            editedPassword.setText(dataForLogin.password)
        }

        // политика обработки данных
        binding.message.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                "https://www.bsvyazi.ru/bsconnect_policy".toUri())
            startActivity(intent)
        }

        binding.vkImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                "https://vk.com/bsvyazi".toUri())
            startActivity(intent)
        }

        // уходим на авторизацию по смс
        binding.messageSmsLogin.setOnClickListener{
            val intent = Intent(this@LoginActivity, LoginByPhoneActivity::class.java)
            startActivity(intent)
        }

        binding.autorization.setOnClickListener {
            if (editedLogin.text.isNullOrBlank() || editedPassword.text.isNullOrBlank()) {
                setMessage(false, R.string.emptyLoginOrPasswordMessage)
            } else {
                val apiClient = ApiClient()
                var token: String?
                lifecycleScope.launch {
                    token = try {
                        apiClient.loginSuspend(editedLogin.text.toString(), editedPassword.text.toString())
                    } catch (e: Exception) {
                        setMessage(false, R.string.ApiRequestFail)
                        null
                    }
                    if (token == null) {
                        setMessage(false, R.string.BadLoginOrPassword)
                    }
                    setMessage(true, R.string.LoadingData)
                    if (token != null) {
                        val userData = try {
                            apiClient.getUserSuspend(token!!)
                        } catch (e: Exception) {
                            setMessage(false, R.string.DataTransferError)
                            null
                        }
                        if (userData == null) setMessage(false, R.string.BadData)
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
                                setMessage(false, R.string.SubscriptionDataError)
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

