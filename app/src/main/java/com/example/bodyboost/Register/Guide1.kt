package com.example.bodyboost.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.R

class Guide1 : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide1)
        val forwardButton = findViewById<Button>(R.id.forward)
        forwardButton.setOnClickListener {
            val intent = Intent(this@Guide1, Guide2::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        }
        val backButton = findViewById<Button>(R.id.back)
        backButton.setOnClickListener{

        }
    }
}