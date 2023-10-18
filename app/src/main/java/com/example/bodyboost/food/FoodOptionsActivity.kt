package com.example.bodyboost.food

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.bodyboost.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FoodOptionsActivity : AppCompatActivity() {

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var timeButton: Button
    private lateinit var continueButton: Button
    private lateinit var completeButton: Button
    private lateinit var dateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_options)

        // findViewById
        timeButton = findViewById(R.id.button_time)
        continueButton = findViewById(R.id.button_continue)
        completeButton = findViewById(R.id.button_complete)
        dateText = findViewById(R.id.textView_date)

        // setOnClickListener
        timeButton.setOnClickListener {
            showTimePicker()
        }
        continueButton.setOnClickListener {
            showToast("返回上一頁")
            finish()
        }
        completeButton.setOnClickListener {
//            addDietRecord()
            showToast("新增成功")
            finish()
        }
    }

    private fun showTimePicker() {

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog( this,
            TimePickerDialog.OnTimeSetListener { _, h, m ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, h)
                selectedCalendar.set(Calendar.MINUTE, m)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeButton.text = timeFormat.format(selectedCalendar.time)

            }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}