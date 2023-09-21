package com.example.bodyboost.Model

import java.io.Serializable

data class Food(
    val id: Int,
    val name: String,
    val calorie: Number,
    val size: Number,
    val unit: String,
    val protein: Number,
    val fat: Number,
    val carb: Number,
    val sodium: Number,
    val modify: Boolean,
    val food_type_id: Int,
    val store_id: Int
) : Serializable
