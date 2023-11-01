package com.example.bodyboost.Food

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitAPI
import com.example.bodyboost.food.CustomFoodActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchFoodActivity : AppCompatActivity() {

    var optionId: Int = 0
    var dietRecords: MutableList<RetrofitAPI.DietRecordData> = FoodListSingleton.dietRecords

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_food)

        // findViewById
        val custom: Button = findViewById(R.id.button_custom)
        val grains: ImageButton = findViewById(R.id.button2)
        val eggMeatFish: ImageButton = findViewById(R.id.button3)
        val vegetables: ImageButton = findViewById(R.id.button4)
        val fruits: ImageButton = findViewById(R.id.button5)
        val dairy: ImageButton = findViewById(R.id.button6)
        val beans: ImageButton = findViewById(R.id.button7)
        val beverages: ImageButton = findViewById(R.id.button8)
        val alcohol: ImageButton = findViewById(R.id.button9)
        val nuts: ImageButton = findViewById(R.id.button10)
        val snacks: ImageButton = findViewById(R.id.button11)
        val fastFood: ImageButton = findViewById(R.id.button12)
        val condiment: ImageButton = findViewById(R.id.button13)
        val dish: ImageButton = findViewById(R.id.button14)
        val others: ImageButton = findViewById(R.id.button15)
        val searchView: EditText = findViewById(R.id.search_food)
        val foodOptions: FloatingActionButton = findViewById(R.id.button_food_options)
        val back: Button = findViewById(R.id.back)

        val clickableViews = listOf(grains, eggMeatFish, vegetables, fruits, dairy, beans, beverages,
            alcohol, nuts, snacks, fastFood, condiment, dish,others)

        // setOnClickListener
        custom.setOnClickListener { navigateToCustomFoodActivity() }
        clickableViews.forEachIndexed { index, view ->
            view.setOnClickListener {
                optionId = index
                Log.i("option ID", "index: $optionId")
                navigateToFoodTypeActivity(optionId)
            }
        }
        foodOptions.setOnClickListener {
            val intent = Intent(this, FoodOptionsActivity::class.java)
            startActivity(intent)
        }
        back.setOnClickListener {
            dietRecords.clear()
            finish()
        }
    }

    private fun navigateToFoodTypeActivity(optionId: Int) {
        val intent = Intent(this, FoodTypeActivity::class.java)
        intent.putExtra("optionId", optionId)
        startActivity(intent)
    }

    private fun navigateToCustomFoodActivity() {
        val intent = Intent(this, CustomFoodActivity::class.java)
        startActivity(intent)
    }
}