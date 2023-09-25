package com.example.bodyboost

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.bodyboost.Model.Food
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomFoodActivity : AppCompatActivity() {

    val currentUser = UserSingleton.user
    private var userId: Int = 0
    private var progressDialog: ProgressDialog? = null
    private val retrofitAPI = RetrofitManager.getInstance()

    private lateinit var customFoodList: List<Food>
    private lateinit var customFoodAdapter: CustomFoodAdapter
    private lateinit var customListView: ListView
    private lateinit var noCustomFood: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_food)
        if (currentUser != null) {
            userId = currentUser.id
        }

        // findViewById
        val addCustomFood = findViewById<Button>(R.id.addCustomFood)
        val foodOptions = findViewById<FloatingActionButton>(R.id.button_food_options)
        val back = findViewById<Button>(R.id.back)
        customListView = findViewById(R.id.customListView)
        noCustomFood = findViewById(R.id.noCustomFood)

        // setOnClickListener
        back.setOnClickListener {
            finish()
        }
        foodOptions.setOnClickListener {
            navigateActivity(FoodOptionsActivity())
        }
        addCustomFood.setOnClickListener {
            navigateActivity(CustomFoodAddActivity())
            finish()
        }

        displayCustomFood()
    }

    private fun displayCustomFood() {
        loadProgressDialog()
        retrofitAPI.searchFoodById("1", userId.toString(), 1, 50).enqueue(object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                displayCustomFoodResponse(response)
            }
            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                showToast("請求失敗：" + t.message)
                t.printStackTrace()
                dismissProgressDialog()
                println(t.message)
            }
        })
    }

    private fun displayCustomFoodResponse(response: Response<List<Food>>) {
        if (response.isSuccessful) {
            val customFood: List<Food>? = response.body()
            if (customFood != null) {
                when (response.code()) {
                    200 -> {
                        noCustomFood.text = ""
                        this.customFoodList = customFood
                        customFoodAdapter = CustomFoodAdapter(this, customFoodList)
                        customFoodListView(customFoodAdapter)
                    }
                    404 -> {
                        noCustomFood.text = "404 Not Found"
                        noCustomFood.setBackgroundColor(Color.WHITE)
                        //showToast("404 錯誤")
                    }
                    else -> {
                        noCustomFood.text = "伺服器錯誤，請稍後再試"
                        noCustomFood.setBackgroundColor(Color.WHITE)
                        //showToast("伺服器故障")
                    }
                }
            } else {
                noCustomFood.text = "尚無自訂食物\n請點選下方按鈕新增"
                noCustomFood.setBackgroundColor(Color.WHITE)
                println(response.toString())
            }
        } else {
            noCustomFood.text = "此類別資料目前有誤"
            noCustomFood.setBackgroundColor(Color.WHITE)
            //showToast("搜尋食物請求失敗：" + response.message())
            println(response.toString())
        }
        dismissProgressDialog()
    }

    private fun customFoodListView(customFoodAdapter: CustomFoodAdapter) {
        customListView.adapter = customFoodAdapter
        customListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(this@CustomFoodActivity, FoodInfoActivity::class.java)
                val customFood: Food = customFoodList[position]
                if (customFood != null) {
                    intent.putExtra("food", customFood)
                    startActivity(intent)
                } else {
                    showToast("食物資料為空")
                }
            }
    }

    private fun navigateActivity(newActivity: Activity) {
        val intent = Intent(this, newActivity::class.java)
        startActivity(intent)
    }

    private fun loadProgressDialog() {
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage("Loading...")
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}