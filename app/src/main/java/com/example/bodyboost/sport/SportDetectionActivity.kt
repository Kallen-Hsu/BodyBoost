package com.example.bodyboost.sport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.bodyboost.R
import org.w3c.dom.Text

class SportDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_detection)

        // get sport data from SportFragment
        val sportId  = intent.getIntExtra("id", 0)
        val sportName = intent.getStringExtra("name").toString()
        val sportType = intent.getStringExtra("sport_type").toString()
        val sportIsCount = intent.getBooleanExtra("is_count", false)
        var time = 0
        var counting = 0
        if (sportType == "timing") {
            time = intent.getIntExtra("time", 0)
        } else if (sportType == "counting") {
            counting = intent.getIntExtra("counting", 0)
        }
        val sportAnimation = intent.getStringExtra("animation").toString()
        val sportMet = intent.getFloatExtra("met", 0.0f)

        val animationView: ImageView = findViewById(R.id.sport_detection_animation)
        val textView1: TextView = findViewById(R.id.sport_detection_text1)
        val textView2: TextView = findViewById(R.id.sport_detection_text2)
        val textView3: TextView = findViewById(R.id.sport_detection_text3)
        val textView4: TextView = findViewById(R.id.sport_detection_text4)

        Glide.with(animationView).load(sportAnimation).into(animationView)
        textView1.text = String.format("name: %s", sportName)
        textView2.text = String.format("sportType: %s", sportType)
        textView3.text = String.format("time: %s", time.toString())
        textView4.text = String.format("counting: %s", counting.toString())
    }
}