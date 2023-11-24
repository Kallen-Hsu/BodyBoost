package com.example.bodyboost

object FoodListSingleton {
    var dateText: String? = null
    var label: String? = null
    var dietRecords: MutableList<RetrofitAPI.DietRecordData> = mutableListOf()
}