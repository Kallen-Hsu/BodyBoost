package com.example.bodyboost.food

import com.example.bodyboost.RetrofitAPI

object FoodListSingleton {
    var dateText: String? = null
    var label: String? = null
    var dietRecords: MutableList<RetrofitAPI.DietRecordData> = mutableListOf()
}