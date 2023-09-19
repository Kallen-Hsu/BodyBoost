package com.example.bodyboost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bodyboost.Model.Food
import com.example.bodyboost.Model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class FoodInfoActivity : AppCompatActivity() {

    val currentUser = UserSingleton.user
    private var userId: Int = 0
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_info)

        val selectedFood = intent.getSerializableExtra("food") as Food

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
            Toast.makeText(this, selectedFood.name + " 新增成功", Toast.LENGTH_SHORT).show()
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
        intakeSize.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            try {
                calculateIntakeSize(selectedFood)
            } catch (e: Exception) {
                println("異常：${e.message}")
            } finally {}

            Toast.makeText(this@FoodInfoActivity, calorie.text, Toast.LENGTH_SHORT).show()
            false
        })
        intakeSize.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {  }
        })
    }

    private fun displayFoodInformation(selectedFood: Food) {
        foodName.text = selectedFood.name
        calorie.text = selectedFood.calorie.toString()
        protein.text = selectedFood.protein.toString()
        carb.text = selectedFood.carb.toString()
        fat.text = selectedFood.fat.toString()
        sodium.text = selectedFood.sodium.toString()
        when (selectedFood.store_id) {
            1 -> storeName.text = ""
            null -> {
                storeName.text = ""
                Toast.makeText(this@FoodInfoActivity, "商店資料為空", Toast.LENGTH_SHORT).show()
            }
            else -> getStoreName(selectedFood.store_id)
        }
    }

    private fun getStoreName(storeId: Int) {
        var toast = Toast(this@FoodInfoActivity)
        val call = retrofitAPI.getAllStore()
        call.enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>, response: Response<List<Store>>) {
                if (response.isSuccessful) {
                    val store: List<Store>? = response.body()
                    if (store != null) {
                        when (response.code()) {
                            200 -> {
                                val storeList = store
                                storeName.text = storeList[storeId].name
                            }
                            404 -> toast.setText("404 錯誤")
                            else -> toast.setText("伺服器故障")
                        }
                    } else {
                        toast.setText("伺服器返回數據為空")
                        println(response.toString())
                    }
                } else {
                    toast.setText("請求失敗 ：" + response.message())
                    println(response.toString())
                }
            }
            override fun onFailure(call: Call<List<Store>>, t: Throwable) {
                toast.setText("請求失敗：" + t.message)
                t.printStackTrace()
                println(t.message)
            }
        })
    }

    private fun calculateIntakeSize(food: Food) {
        if (!isNumberNullOrZero(food.calorie)) {
            calorie.text = (food.calorie.toString().toFloat() * intakeSize.text.toString().toFloat() / 100).toString()
        } else {
            calorie.text = "0.0"
        }
        if (!isNumberNullOrZero(food.protein)) {
            protein.text = (food.protein.toString().toFloat() * intakeSize.text.toString().toFloat() / 100).toString()
        } else {
            protein.text = "0.0"
        }
        if (!isNumberNullOrZero(food.carb)) {
            carb.text = (food.carb.toString().toFloat() * intakeSize.text.toString().toFloat() / 100).toString()
        } else {
            carb.text = "0.0"
        }
        if (!isNumberNullOrZero(food.fat)) {
            fat.text = (food.fat.toString().toFloat() * intakeSize.text.toString().toFloat() / 100).toString()
        } else {
            fat.text = "0.0"
        }
        if (!isNumberNullOrZero(food.sodium)) {
            sodium.text = (food.sodium.toString().toFloat() * intakeSize.text.toString().toFloat() / 100).toString()
        } else {
            sodium.text = "0.0"
        }
    }

    private fun isNumberNullOrZero(number: Float?) : Boolean {
        return number == null || number == 0.0f
    }
}
