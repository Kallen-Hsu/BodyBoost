package com.example.bodyboost

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.bodyboost.Model.CustomFood
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomFoodAddActivity : AppCompatActivity() {

    val currentUser = UserSingleton.user
    private var userId: Int = 0
    private var progressDialog: ProgressDialog? = null
    private val retrofitAPI = RetrofitManager.getInstance()

    private var unit: String = "g"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_food_add)
        if (currentUser != null) {
            userId = currentUser.id
        }

        // findViewById
        val back = findViewById<Button>(R.id.back)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val inputFinished = findViewById<Button>(R.id.inputFinished)
        val foodName = findViewById<EditText>(R.id.foodName)
        val foodCalorie = findViewById<EditText>(R.id.foodCalorie)
        val foodSize = findViewById<EditText>(R.id.foodSize)

        // setOn
        back.setOnClickListener {
            navigateActivity(CustomFoodActivity())
            finish()
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_g -> unit = "g"
                R.id.radioButton_ml -> unit = "ml"
            }
        }
        inputFinished.setOnClickListener {
            val name = foodName.text.toString()
            val calorie = foodCalorie.text.toString().toDouble()
            val size = foodSize.text.toString().toDouble()
            val protein = getEditTextValue(R.id.foodProtein).toDoubleOrNull()
            val fat = getEditTextValue(R.id.foodFat).toDoubleOrNull()
            val carb = getEditTextValue(R.id.foodCarb).toDoubleOrNull()
            val sodium = getEditTextValue(R.id.foodSodium).toDoubleOrNull()
            if (editTextValidate(foodName, foodCalorie, foodSize)) {
                try {
                    addCustomFood(name, calorie, size, unit, protein, fat, carb, sodium, true, userId)
                } catch (e : Exception) {
                    println("異常：${e.message}")
                    showToast("新增自訂食物失敗")
                }
            }
        }
    }

    private fun addCustomFood(name: String, calorie: Number, size: Number, unit: String, protein: Number?, fat: Number?, carb: Number?, sodium: Number?, modify: Boolean, userID: Int) {
        loadProgressDialog()
        val addCustomFoodData = RetrofitAPI.AddCustomFoodData(name, calorie, size, unit, protein, fat, carb, sodium, modify, 1, 1, userID)
        retrofitAPI.addCustomFood(addCustomFoodData).enqueue(object : Callback<CustomFood> {
            override fun onResponse(call: Call<CustomFood>, response: Response<CustomFood>) {
                addCustomFoodResponse(response)
            }
            override fun onFailure(call: Call<CustomFood>, t: Throwable) {
                showToast("新增食物請求失敗：" + t.message)
                t.printStackTrace()
                dismissProgressDialog()
                println(t.message)
            }
        })
    }

    private fun addCustomFoodResponse(response: Response<CustomFood>) {
        if (response.isSuccessful) {
            val customFood: CustomFood? = response.body()
            if (customFood != null) {
                when (response.code()) {
                    200 -> {
                        showToast("新增完成")
                        navigateActivity(CustomFoodActivity())
                        finish()
                    }
                    404 -> showToast("404 錯誤")
                    else -> showToast("伺服器故障")
                }
            } else {
                showToast("新增自訂食物失敗")
                println(response.toString())
            }
        } else {
            showToast("新增自訂食物請求失敗：" + response.message())
            println(response.toString())
        }
        dismissProgressDialog()
    }

    private fun getEditTextValue(viewId: Int): String {
        val editText = findViewById<EditText>(viewId)
        return editText.text.toString()
    }

    private fun editTextValidate(vararg editTexts: EditText): Boolean {
        // check EditText is Empty or Blank
        for (editText in editTexts) {
            val text = editText.text.toString()
            if (text.isBlank()) {
                editText.error = "此項目為必填"
                return false
            }
        }
        return true
    }

    private fun navigateActivity(newActivity: Activity) {
        val intent = Intent(this, newActivity::class.java)
        startActivity(intent)
    }

    private fun loadProgressDialog() {
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage("正在新增自訂食物...")
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