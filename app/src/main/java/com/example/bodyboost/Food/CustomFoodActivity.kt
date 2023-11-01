package com.example.bodyboost.food

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import com.example.bodyboost.Food.CustomFoodAddActivity
import com.example.bodyboost.Food.FoodInfoActivity
import com.example.bodyboost.Food.FoodListAdapter
import com.example.bodyboost.Food.FoodOptionsActivity
import com.example.bodyboost.Model.CustomFood
import com.example.bodyboost.Model.Food
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
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
    private lateinit var customFoodAdapter: FoodListAdapter
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
        retrofitAPI.searchFoodById("1", userId.toString(), 1, 50)
            .enqueue(object : Callback<List<Food>> {
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
                        customFoodAdapter = FoodListAdapter(this, customFoodList)
                        customFoodListView(customFoodAdapter)
                    }

                    404 -> {
                        noCustomFood.text = "Not Found"
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

    private fun customFoodListView(customFoodAdapter: FoodListAdapter) {
        customListView.adapter = customFoodAdapter
        customListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(this@CustomFoodActivity, FoodInfoActivity::class.java)
                val customFood: Food = customFoodList[position]
                intent.putExtra("food", customFood)
                startActivity(intent)
            }
        customListView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                deleteCustomFoodDialog(customFoodList[position])
                true
            }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteCustomFoodDialog(customFood: Food) {
        val builder = AlertDialog.Builder(this@CustomFoodActivity)
        builder.setTitle("刪除自訂食物")

        val view = layoutInflater.inflate(R.layout.dialog_delete_custom_food, null)
        builder.setView(view)

        val isDelete = view.findViewById<TextView>(R.id.delete_custom_food)
        val foodName = customFood.name
        isDelete.text = "是否刪除「$foodName」？"

        builder.setPositiveButton("刪除") { dialog, _ ->
            deleteCustomFood(customFood.id)
            displayCustomFood()
        }
        builder.setNegativeButton("取消", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteCustomFood(id: Int) {
        retrofitAPI.deleteCustomFood(id.toString()).enqueue(object : Callback<List<CustomFood>> {
            override fun onResponse(call: Call<List<CustomFood>>, response: Response<List<CustomFood>>) {
                deleteCustomFoodResponse(response)
            }

            override fun onFailure(call: Call<List<CustomFood>>, t: Throwable) {
                showToast("刪除失敗：" + t.message)
                t.printStackTrace()
                dismissProgressDialog()
                println(t.message)
            }
        })
    }

    private fun deleteCustomFoodResponse(response: Response<List<CustomFood>>) {
        if (response.isSuccessful) {
            when (response.code()) {
                200 -> {
                    showToast("刪除成功")
                }

                404 -> showToast("Not Found")
                else -> showToast("伺服器錯誤，請稍後再試")
            }
        } else {
            showToast("刪除自訂食物失敗：" + response.message())
            println(response.toString())
        }
        dismissProgressDialog()
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