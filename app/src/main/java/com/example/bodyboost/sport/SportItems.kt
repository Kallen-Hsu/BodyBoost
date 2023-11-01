package com.example.bodyboost.sport

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bodyboost.R

class SportItems: AppCompatActivity()  {
    private lateinit var animate: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_info_timing)


        animate = findViewById(R.id.animate)
        Glide.with(animate).load("https://storage.googleapis.com/bodyboost-bucket/animation_video/Lisa-elbow-knee-touch2-light-unscreen.gif").into(animate)

    }

}