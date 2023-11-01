package com.example.bodyboost.sport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bodyboost.R

class SportInfoActivity : AppCompatActivity() {
    private lateinit var animate: ImageView
    private lateinit var backBtn: Button
    private lateinit var lastingBtn: Button
    private lateinit var setting_time: TextView
    private lateinit var plusBtn: ImageButton
    private lateinit var minusBtn: ImageButton
    private var lateinitTime = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_info_timing)

        val MainButtonColor = getColor(R.color.Second)
        val MainTextColor = getColor(R.color.white)

        animate = findViewById(R.id.animate)
        Glide.with(animate).load("https://storage.googleapis.com/bodyboost-bucket/animation_video/Lisa-elbow-knee-touch2-light-unscreen.gif").into(animate)

        backBtn = findViewById(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this@SportInfoActivity, SportFragment::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        }

        lastingBtn = findViewById(R.id.lastingBtn)
        lastingBtn.setOnClickListener {
            lastingBtn.setBackgroundColor(MainButtonColor)
            lastingBtn.setTextColor(MainTextColor)
        }

        setting_time = findViewById(R.id.setting_time)
        plusBtn = findViewById(R.id.plusBtn)
        plusBtn.setOnClickListener {
            addTime(5)
        }

        minusBtn = findViewById(R.id.minusBtn)
        minusBtn.setOnClickListener {
            subtractTime(5)
        }

        updateTimerText()
    }

    private fun addTime(seconds: Int) {
        lateinitTime += seconds
        updateTimerText()
    }

    private fun subtractTime(seconds: Int) {
        if (lateinitTime > seconds) {
            lateinitTime -= seconds
        } else {
            lateinitTime = 0
        }
        updateTimerText()
    }

    private fun updateTimerText() {
        val minutes = lateinitTime / 60
        val seconds = lateinitTime % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)
        setting_time.text = timeString
    }
}
