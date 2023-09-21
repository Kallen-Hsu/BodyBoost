package com.example.bodyboost.Model

import java.io.Serializable

data class CustomFood(
    val id: Int,
    val name: String,
    val calorie: Number,
    val size: Number,
    val unit: String,
    val protein: Number?,
    val fat: Number?,
    val carb: Number?,
    val sodium: Number?,
    val modify: Boolean,
    val type_id: Int,
    val store_id: Int,
    val user_id: Int
) : Serializable
