package com.example.bodyboost.Model

data class Profile(
    val id: Number,
    val name: String,
    val gender: Int,
    val birthday: String,
    val height: Number,
    var weight: Number,
    var weight_goal: Number,
    val image:String,
    var body_fat:Number,
    var goal:String,
    val userID: Int // 關聯到 User 資料表中的 id 欄位
)
