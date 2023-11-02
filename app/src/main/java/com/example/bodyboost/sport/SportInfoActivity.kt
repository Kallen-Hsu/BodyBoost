package com.example.bodyboost.sport

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.bodyboost.R
import com.example.bodyboost.sport.SportFragment
import org.w3c.dom.Text
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class SportInfoActivity : AppCompatActivity() {
    private lateinit var animate: ImageView
    private lateinit var backBtn: Button
    private lateinit var lastingBtn: Button
    private lateinit var setting_time: TextView
    private lateinit var setting_counting: EditText
    private lateinit var plusBtn: ImageButton
    private lateinit var minusBtn: ImageButton
    private lateinit var sportInfoSwitch: LinearLayout
    private lateinit var timesBtn: Button
    private lateinit var sportInfoSwitchTitle: TextView
    private lateinit var startSportButton: Button
    private var lateinitTime = 0

    private var sportId: Int = 0
    private var sportName: String = ""
    private var sportDescription: String = ""
    private var sportDefaultTime: Float = 0.0f
    private var sportInterval: Float = 0.0f
    private var sportIsCount: Boolean = false
    private var sportAnimation: String = ""
    private var sportMet: Float = 0.0f

    private var sportType: String = "timing"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_info)

        // get sport data from SportFragment
        sportId  = intent.getIntExtra("id", 0)
        sportName = intent.getStringExtra("name").toString()
        sportDescription = intent.getStringExtra("description").toString()
        sportDefaultTime = intent.getFloatExtra("default_time", 0.0f)
        sportInterval = intent.getFloatExtra("interval", 0.0f)
        sportIsCount = intent.getBooleanExtra("is_count", false)
        sportAnimation = intent.getStringExtra("animation").toString()
        sportMet = intent.getFloatExtra("met", 0.0f)

        // set default sport time
        lateinitTime = sportDefaultTime.toInt()

        val MainButtonColor = getColor(R.color.Second)
        val MainTextColor = getColor(R.color.white)
        val sportNameTextView: TextView = findViewById(R.id.textView)
        val sportDescriptionTextView: TextView = findViewById(R.id.textView6)

        // set sport title, description
        sportNameTextView.text = sportName
        sportDescriptionTextView.text = sportDescription

        // set sport animation
        animate = findViewById(R.id.animate)
        Glide.with(animate).load(sportAnimation).into(animate)

        sportInfoSwitch = findViewById(R.id.sport_info_switch)
        sportInfoSwitchTitle = findViewById(R.id.sport_info_switch_title)
        setting_counting = findViewById(R.id.setting_counting)

        backBtn = findViewById(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this@SportInfoActivity, SportFragment::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        }

        lastingBtn = findViewById(R.id.lastingBtn)
        lastingBtn.setOnClickListener {
            sportType = "timing"
            changeSwitchColor("timing")
            changeSwitchContainer("timing")
        }

        timesBtn = findViewById(R.id.timesBtn)
        if (sportIsCount) {
            timesBtn.setOnClickListener {
                sportType = "counting"
                changeSwitchColor("counting")
                changeSwitchContainer("counting")
            }
        } else {
            timesBtn.visibility = View.GONE
        }

        setting_time = findViewById(R.id.setting_time)
        plusBtn = findViewById(R.id.plusBtn)
        plusBtn.setOnClickListener {
            addTime(sportInterval.toInt())
        }

        minusBtn = findViewById(R.id.minusBtn)
        minusBtn.setOnClickListener {
            subtractTime(sportInterval.toInt())
        }

        startSportButton = findViewById(R.id.start_sport_button)
        startSportButton.setOnClickListener{
            val intent = Intent(this@SportInfoActivity, SportDetectionActivity::class.java)
            intent.putExtra("id", sportId)
            intent.putExtra("name", sportName)
            if (sportType == "timing") {
                intent.putExtra("time", lateinitTime)
                intent.putExtra("sport_type", sportType)
                intent.putExtra("is_count", sportIsCount)
                intent.putExtra("met", sportMet)
                intent.putExtra("animation", sportAnimation)
                startActivity(intent)
            } else if (sportType == "counting"){
                if (setting_counting.text.isNullOrEmpty()) {
                    Toast.makeText(this, "請輸入次數", Toast.LENGTH_SHORT).show()
                } else {
                    intent.putExtra("counting", setting_counting.text.toString().toInt())
                    intent.putExtra("sport_type", sportType)
                    intent.putExtra("is_count", sportIsCount)
                    intent.putExtra("met", sportMet)
                    intent.putExtra("animation", sportAnimation)
                    startActivity(intent)
                }
            }
        }

        updateTimerText()
    }

    private fun changeSwitchColor(state: String) {
        if (state == "timing") {
            sportInfoSwitchTitle.text = "時間"
            lastingBtn.setBackgroundColor(ContextCompat.getColor(applicationContext!!, R.color.Second))
            lastingBtn.setTextColor(getColor(R.color.white))
            timesBtn.setBackgroundColor(ContextCompat.getColor(applicationContext!!, R.color.Yellow))
            timesBtn.setTextColor(getColor(R.color.Second))
        } else if (state == "counting") {
            sportInfoSwitchTitle.text = "次數"
            lastingBtn.setBackgroundColor(ContextCompat.getColor(applicationContext!!, R.color.Yellow))
            lastingBtn.setTextColor(getColor(R.color.Second))
            timesBtn.setBackgroundColor(ContextCompat.getColor(applicationContext!!, R.color.Second))
            timesBtn.setTextColor(getColor(R.color.white))
        }
    }

    private fun changeSwitchContainer(state: String) {
        if (state == "timing") {
            sportInfoSwitch.visibility = View.VISIBLE
            setting_counting.visibility = View.GONE
            setting_time = findViewById(R.id.setting_time)
        } else if (state == "counting") {
            sportInfoSwitch.visibility = View.GONE
            setting_counting.visibility = View.VISIBLE
        }
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
