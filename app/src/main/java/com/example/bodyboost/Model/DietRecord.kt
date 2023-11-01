package com.example.bodyboost.Model

import com.google.type.DateTime
import java.io.Serializable

data class DietRecord(
    val id: Int,
    val date: DateTime,
    val label: String,
    val serving_amount: Number,
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
    val store_id: Int,
    val user_id: Int
) : Serializable
