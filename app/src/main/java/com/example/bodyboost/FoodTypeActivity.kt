package com.example.bodyboost

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.Model.Food
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodTypeActivity : AppCompatActivity() {

    val currentUser = UserSingleton.user
    private var userId: Int = 0
    private val receivedIntent = intent
    private val retrofitAPI = RetrofitManager.getInstance()
    private var progressDialog: ProgressDialog? = null

    private lateinit var foodList: List<Food>
    private lateinit var foodListAdapter: FoodListAdapter

    private lateinit var listView: ListView
    private lateinit var noFood: TextView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_type)
        val spinnerItems = listOf("五穀澱粉類", "蛋肉魚類", "蔬菜類", "水果類", "乳品類", "豆類", "飲料類", "酒類", "油脂與堅果類", "零食點心", "速食類", "調味品", "菜餚類", "其他類別")
        if (currentUser != null) {
            userId = currentUser.id
        }
        var optionId: Int = 0
        if (receivedIntent != null) {
        }

        // findViewById
        val spinner = findViewById<Spinner>(R.id.spinner_type)
        val foodOptions = findViewById<FloatingActionButton>(R.id.button_food_options)
        val back = findViewById<Button>(R.id.back)
        listView = findViewById(R.id.customListView)
        noFood = findViewById(R.id.noFood)

        // set spinner
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        if (receivedIntent != null) {
            optionId = receivedIntent.getIntExtra("optionId", 0)
        }
        spinner.setSelection(optionId)

        // setOnClickListener
        back.setOnClickListener {
            finish()
        }
        foodOptions.setOnClickListener {
            navigateActivity(FoodOptionsActivity())
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val option = spinnerItems[position]
                displayFood(position + 2)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun displayFood(foodId: Int) {
        loadProgressDialog()
        retrofitAPI.searchFoodById(foodId.toString(), userId.toString(), 1, 50).enqueue(object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                displayFoodResponse(response)
            }
            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                showToast("請求失敗：" + t.message)
                t.printStackTrace()
                dismissProgressDialog()
                println(t.message)
            }
        })
    }

    private fun displayFoodResponse(response: Response<List<Food>>) {
        if (response.isSuccessful) {
            val food: List<Food>? = response.body()
            if (food != null) {
                when (response.code()) {
                    200 -> {
                        noFood.text = ""
                        noFood.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                        this.foodList = food
                        foodListAdapter = FoodListAdapter(this, foodList)
                        foodListView(foodListAdapter)
                    }
                    404 -> {
                        noFood.text = "404 Not Found"
                        noFood.setBackgroundColor(Color.WHITE)
                        //showToast("404 錯誤")
                    }
                    else -> {
                        noFood.text = "伺服器錯誤，請稍後再試"
                        noFood.setBackgroundColor(Color.WHITE)
                        //showToast("伺服器故障")
                    }
                }
            } else {
                noFood.text = "此類別目前尚無資料"
                noFood.setBackgroundColor(Color.WHITE)
                //showToast("伺服器返回數據為空")
                println(response.toString())
            }
        } else {
            noFood.text = "此類別資料有誤"
            noFood.setBackgroundColor(Color.WHITE)
            //showToast("搜尋食物請求失敗 " + response.message())
            println(response.toString())
        }
        dismissProgressDialog()
    }

    private fun foodListView(foodListAdapter: FoodListAdapter) {
        listView.adapter = foodListAdapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(this@FoodTypeActivity, FoodInfoActivity::class.java)
                val food: Food = foodList[position]
                intent.putExtra("food", food)
                startActivity(intent)
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

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}