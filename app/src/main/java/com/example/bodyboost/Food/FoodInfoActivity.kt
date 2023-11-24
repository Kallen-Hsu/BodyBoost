package com.example.bodyboost.Food

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bodyboost.FoodListSingleton
import com.example.bodyboost.Model.Food
import com.example.bodyboost.Model.Store
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitAPI
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FoodInfoActivity : AppCompatActivity() {

    val currentUser = UserSingleton.user
    private val retrofitAPI = RetrofitManager.getInstance()
    private lateinit var back: Button
    private lateinit var add: Button
    private lateinit var storeName: TextView
    private lateinit var foodName: TextView
    private lateinit var unit: TextView
    private lateinit var calorie: TextView
    private lateinit var protein: TextView
    private lateinit var carb: TextView
    private lateinit var fat: TextView
    private lateinit var sodium: TextView
    private lateinit var intakeSize: EditText
    private var dietRecords: MutableList<RetrofitAPI.DietRecordData> = FoodListSingleton.dietRecords
    private lateinit var dietRecord: RetrofitAPI.DietRecordData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)

        val userId: Int
        val selectedFood = intent.getSerializableExtra("food") as Food
        if (currentUser != null) {
            userId = currentUser.id
        }

        // findViewById
        back = findViewById(R.id.backButton)
        add = findViewById(R.id.addButton)
        storeName = findViewById(R.id.storeName)
        foodName = findViewById(R.id.foodName)
        unit = findViewById(R.id.unit)
        calorie = findViewById(R.id.calorie)
        protein = findViewById(R.id.protein)
        carb = findViewById(R.id.carb)
        fat = findViewById(R.id.fat)
        sodium = findViewById(R.id.sodium)
        intakeSize = findViewById(R.id.intakeSize)

        displayFoodInformation(selectedFood)

        // setOnClickListener
        add.setOnClickListener {
            addDietRecord(
                intakeSize.text.toString().toDouble(),
                selectedFood
            )
            finish()
        }
        back.setOnClickListener { finish() }
        unit.setOnClickListener {
            if (selectedFood.modify) {
                if (unit.text == "克") {
                    unit.text = "份"
                } else {
                    unit.text = "克"
                }
            }
        }
        intakeSize.setOnEditorActionListener { _, _, _ ->
            try {
                changeIntakeSize(selectedFood)
            } catch (e: Exception) {
                println("異常：${e.message}")
            } finally {
            }
            false
        }
        intakeSize.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun displayFoodInformation(selectedFood: Food) {
        foodName.text = selectedFood.name
        calorie.text = selectedFood.calorie.toString()
        protein.text = isNumberNull(selectedFood.protein).toString()
        carb.text = isNumberNull(selectedFood.carb).toString()
        fat.text = isNumberNull(selectedFood.fat).toString()
        sodium.text = isNumberNull(selectedFood.sodium).toString()
        when (selectedFood.store_id) {
            1 -> storeName.text = ""
            else -> getStoreName(selectedFood.store_id)
        }
    }

    private fun getStoreName(storeId: Int) {
        val toast = Toast(this@FoodInfoActivity)
        val call = retrofitAPI.getAllStore()
        call.enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                if (response.isSuccessful) {
                    val store: List<Store>? = response.body()
                    if (store != null) {
                        when (response.code()) {
                            200 -> {
                                storeName.text = store[storeId].name
                            }
                            404 -> toast.setText("404 錯誤")
                            else -> toast.setText("伺服器故障")
                        }
                    } else {
                        println("伺服器返回數據為空$response")
                    }
                } else {
                    println("請求失敗 ：$response")
                }
            }
            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                t.printStackTrace()
                println("請求失敗：" + t.message)
            }
        })
    }

    private fun changeIntakeSize(food: Food) {
        if (!isNumberNullOrZero(food.calorie)) {
            calorie.text = calculateIntakeSize(food.calorie)
        } else {
            calorie.text = "0.0"
        }
        if (!isNumberNullOrZero(food.protein)) {
            protein.text = calculateIntakeSize(food.protein)
        } else {
            protein.text = "0.0"
        }
        if (!isNumberNullOrZero(food.carb)) {
            carb.text = calculateIntakeSize(food.carb)
        } else {
            carb.text = "0.0"
        }
        if (!isNumberNullOrZero(food.fat)) {
            fat.text = calculateIntakeSize(food.fat)
        } else {
            fat.text = "0.0"
        }
        if (!isNumberNullOrZero(food.sodium)) {
            sodium.text = calculateIntakeSize(food.sodium)
        } else {
            sodium.text = "0.0"
        }
    }

    private fun calculateIntakeSize(number: Number): String {
        val num = (number.toDouble() * intakeSize.text.toString().toDouble()) / 100.0
        return String.format("%.1f", num)
    }

    private fun addDietRecord(
        serving_amount: Number,
        food: Food,
    ) {
        dietRecord = RetrofitAPI.DietRecordData(
            serving_amount,
            food.name,
            calorie.text.toString().toDouble(),
            serving_amount,
            unit.text.toString(),
            protein.text.toString().toDouble(),
            fat.text.toString().toDouble(),
            carb.text.toString().toDouble(),
            sodium.text.toString().toDouble(),
            food.modify,
            food.food_type_id,
            food.store_id
        )
        dietRecords.add(dietRecord)
        Toast.makeText(this, food.name + " 新增成功", Toast.LENGTH_SHORT).show()
    }

    private fun isNumberNullOrZero(number: Number?): Boolean {
        return number == null || number == 0.0
    }

    private fun isNumberNull(number: Number?): Number {
        // check number is null
        return number ?: 0.0
    }
}