package com.example.bodyboost.sport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.R

class SportFinishedActivity :AppCompatActivity(){
    private lateinit var back_to_mainBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sportfinished)
        val main_page = findViewById<Button>(R.id.back_to_main)
        main_page.setOnClickListener {
            startSportActivity()
        }
    }
    private fun startSportActivity() {
        val intent = Intent(this@SportFinishedActivity, SportFragment::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        back_to_mainBtn = findViewById(R.id.back_to_main)
        back_to_mainBtn.setOnClickListener {
            val intent = Intent(this@SportFinishedActivity, SportFragment::class.java)
            startActivity(intent)
        }
    }
}