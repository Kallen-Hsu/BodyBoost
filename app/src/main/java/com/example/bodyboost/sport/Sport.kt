package com.example.bodyboost.sport

data class Sport(
    val id: Int,
    val name: String,
    val description: String,
    val default_time: Float,
    val interval: Float,
    val is_count: Boolean,
    val met: Float,
    val type: String,
    val animation: Animation
) {
    data class Animation (
        val id: Int,
        val name: String,
        val animation: String,
        val image: String,
        val sport_id: Int)
}