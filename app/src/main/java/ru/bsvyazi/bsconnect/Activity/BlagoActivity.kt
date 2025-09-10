package ru.bsvyazi.bsconnect.Activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ru.bsvyazi.bsconnect.R
import ru.bsvyazi.bsconnect.databinding.ActivityBlagoBinding

class BlagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBlagoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val messageTextView: TextView = findViewById(R.id.message)

        fun setMessage(status: Boolean, linkResMessage: Int) {
            // установка цвета сообщения text_color - normal, alert - error
            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            if (status) messageTextView.setTextColor(getColor(R.color.text_color))
            else messageTextView.setTextColor(ContextCompat.getColor(this, R.color.alert))
            messageTextView.text = getString(linkResMessage)
        }

        if (true) {
            setMessage(true, R.string.CreditIsOnMsg)
        } else {
            setMessage(false, R.string.CreditFailMsg)
        }
        binding.ret.setOnClickListener {
            val intent = Intent(this@BlagoActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}