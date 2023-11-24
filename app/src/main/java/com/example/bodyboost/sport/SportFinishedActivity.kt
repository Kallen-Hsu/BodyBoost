package com.example.bodyboost.sport

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.MainActivity
import com.example.bodyboost.R
import org.w3c.dom.Text
import java.text.DecimalFormat

class SportFinishedActivity :AppCompatActivity(){
    private lateinit var back_to_mainBtn:Button
    private lateinit var sport_time: TextView
    private lateinit var nameTextView: TextView
    private lateinit var consume_calorie: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sportfinished)
        val main_page = findViewById<Button>(R.id.back_to_main)
        main_page.setOnClickListener {
            intent.setClass(this@SportFinishedActivity, MainActivity::class.java)
//            intent.putExtra("index", 1)
            startActivity(intent)
        }

        sport_time = findViewById(R.id.sport_time)
        nameTextView = findViewById(R.id.sport_type)
        consume_calorie = findViewById(R.id.consume_calorie)
        val seconds = intent.getStringExtra("seconds")
        val name = intent.getStringExtra("name")
        val met = intent.getFloatExtra("met", 0.0f)
        sport_time.text = seconds
        nameTextView.text = name

//        String[] split = seconds.split(":")
//        String string2  = split[0]
//        int min = Integer.parseInt(string2)
//        int Mins = min * 60
//        int SS = Integer.parseInt(split[2])
//        totalss = Mins+SS

        // 將秒數字串拆分為陣列
        val split = seconds?.split(":")

        // 取得分鐘
        val min = split?.get(0)?.toInt()

        // 將分鐘轉換為秒
        val Mins = min?.times(60)

        // 取得秒
        val SS = split?.get(1)?.toInt()

        // 計算總秒數
        val totalss = SS?.let { Mins?.plus(it) }

        if (totalss != null) {
            val calorie = (totalss.toFloat() / 60.0 / 60.0) * met * 60.0
            val decimalFormat = DecimalFormat("0.00")
            val formattedFloatValue = decimalFormat.format(calorie)
            consume_calorie.text = formattedFloatValue.toString()
        }
    }
    private fun startSportActivity() {
        val intent = Intent()
        intent.setClass(this@SportFinishedActivity, SportFragment::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        back_to_mainBtn = findViewById(R.id.back_to_main)
        back_to_mainBtn.setOnClickListener {
            intent.setClass(this@SportFinishedActivity, MainActivity::class.java)
//            intent.putExtra("index", 1)
            startActivity(intent)
//            finish()
        }
    }
}