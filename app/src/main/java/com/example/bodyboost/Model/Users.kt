package com.example.bodyboost.Model


data class Users(
    val id: Int,
    val account: String,
    var password: String,
    var email: String,
    val created_type: String,
    val status: String,
    val created_at: String
)


