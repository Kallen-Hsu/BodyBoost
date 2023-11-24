package com.example.bodyboost.Food

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.bodyboost.FoodListSingleton
import com.example.bodyboost.Model.DietRecord
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitAPI
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FoodOptionsActivity : AppCompatActivity() {

    val currentUser = UserSingleton.user
    var userId: Int = 0
    private var progressDialog: ProgressDialog? = null
    private val retrofitAPI = RetrofitManager.getInstance()

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var timeButton: Button
    private lateinit var continueButton: Button
    private lateinit var completeButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var listView: ListView
    private lateinit var foodOptionAdapter: FoodOptionAdapter
    private var dietRecords: MutableList<RetrofitAPI.DietRecordData> = FoodListSingleton.dietRecords

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_options)
        if (currentUser != null) {
            userId = currentUser.id
        }

        // findViewById
        timeButton = findViewById(R.id.button_time)
        continueButton = findViewById(R.id.button_continue)
        completeButton = findViewById(R.id.button_complete)
        dateTextView = findViewById(R.id.textView_date)
        listView = findViewById(R.id.option_list)
        val label = findViewById<TextView>(R.id.textView_label)

        displayFoodList()

        dateTextView.text = showDateText(FoodListSingleton.dateText!!)
        label.text = FoodListSingleton.label!!
        showButtonTime(label.text.toString())

        // setOnClickListener
        timeButton.setOnClickListener {
            showTimePicker()
        }
        continueButton.setOnClickListener {
            showToast("繼續新增")
            finish()
        }
        completeButton.setOnClickListener {
            // 時間設定
            val date: String = FoodListSingleton.dateText + " " + timeButton.text + ":00"

            // 判斷購物車是否有食物
            if (dietRecords.isNotEmpty()) {
                try {
                    addManyDietRecord(date, label.text.toString(), dietRecords)
                } catch (e: Exception) {
                    println("異常：${e.message}")
                    showToast("新增飲食紀錄失敗")
                }
            } else {
                showToast("購物車為空，請加入食物")
            }
        }
    }

    private fun addManyDietRecord(
        dateText: String,
        label: String,
        dietRecords: MutableList<RetrofitAPI.DietRecordData>
    ) {
        loadProgressDialog()
        val dietRecordDataMany = RetrofitAPI.DietRecordDataMany(
            dateText, label, userId, dietRecords
        )
        retrofitAPI.addManyDietRecord(dietRecordDataMany).enqueue(object : Callback<DietRecord> {
            override fun onResponse(call: Call<DietRecord>, response: Response<DietRecord>) {
                addManyDietRecordResponse(response)
            }

            override fun onFailure(call: Call<DietRecord>, t: Throwable) {
                showToast("新增完成-1")
                t.printStackTrace()
                dismissProgressDialog()
                println("新增飲食紀錄請求失敗：" + t.message)
                dietRecords.clear()
                finish()
            }
        })
    }

    private fun addManyDietRecordResponse(response: Response<DietRecord>) {
        if (response.isSuccessful) {
            val dietRecord: DietRecord? = response.body()
            if (dietRecord != null) {
                showToast("新增成功")
                dietRecords.clear()
            } else {
                showToast("新增完成-2")
                println("新增紀錄失敗$response")
            }
        } else {
            showToast("新增完成-3")
            println("新增紀錄請求失敗：$response")
        }
        dismissProgressDialog()
        dietRecords.clear()
        finish()
    }

    private fun displayFoodList() {
        foodOptionAdapter = FoodOptionAdapter(this, dietRecords)
        listView.adapter = foodOptionAdapter
        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                deleteRecordDialog(dietRecords[position], position)
                true
            }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteRecordDialog(dietRecordData: RetrofitAPI.DietRecordData, id: Int) {
        val builder = AlertDialog.Builder(this@FoodOptionsActivity)
        builder.setTitle("刪除食物")

        val view = layoutInflater.inflate(R.layout.dialog_delete_custom_food, null)
        builder.setView(view)

        val isDelete = view.findViewById<TextView>(R.id.delete_custom_food)
        val foodName = dietRecordData.name
        isDelete.text = "是否刪除「$foodName」？"

        builder.setPositiveButton("刪除") { _, _ ->
            dietRecords.removeAt(id)
            displayFoodList()
        }
        builder.setNegativeButton("取消", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun showTimePicker() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener { _, h, m ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, h)
                selectedCalendar.set(Calendar.MINUTE, m)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                timeButton.text = timeFormat.format(selectedCalendar.time)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showButtonTime(label: String) {
        when (label) {
            "早餐" -> timeButton.text = "08:00"
            "午餐" -> timeButton.text = "12:00"
            "晚餐" -> timeButton.text = "18:00"
            "點心/其他" -> timeButton.text = "15:00"
        }
    }

    private fun showDateText(dateText: String): String {
        val data = dateText.split(' ')

        if (data.size > 0) {
            val date = data[0]
            FoodListSingleton.dateText = date
            if (date.matches("\\d{4}-\\d{2}-\\d{2}".toRegex())) {
                val dateData = date.split('-')
                val year = dateData[0]
                val month = dateData[1]
                val day = dateData[2]
                return (year + "年" + month + "月" + day + "日")
            } else {
                return "2023年11月10日"
            }
        } else {
            return "2023年10月11日"
        }
    }

    private fun loadProgressDialog() {
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage("正在新增紀錄...")
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