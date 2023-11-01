package com.example.bodyboost.Food

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
    private lateinit var dateText: TextView
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
        dateText = findViewById(R.id.textView_date)
        listView = findViewById(R.id.option_list)
        var label = findViewById<TextView>(R.id.textView_label)

        displayFoodList()

        dateText.text = showDateText(FoodListSingleton.dateText!!)
        label.text = FoodListSingleton.label
        showButtonTime(label.text.toString())

        // setOnClickListener
        timeButton.setOnClickListener {
            showTimePicker()
        }
        continueButton.setOnClickListener {
            showToast("返回上一頁")
            finish()
        }
        completeButton.setOnClickListener {
            try {
                addDietRecord(dietRecords)
            } catch (e : Exception) {
                println("異常：${e.message}")
                showToast("新增飲食紀錄失敗")
            }
        }
    }

    private fun addDietRecord(dietRecords: MutableList<RetrofitAPI.DietRecordData>) {
        loadProgressDialog()
        retrofitAPI.addDietRecord(dietRecords[0]).enqueue(object : Callback<DietRecord> {
            override fun onResponse(call: Call<DietRecord>, response: Response<DietRecord>) {
                addDietRecordResponse(response)
            }
            override fun onFailure(call: Call<DietRecord>, t: Throwable) {
                showToast("新增飲食紀錄請求失敗：" + t.message)
                t.printStackTrace()
                dismissProgressDialog()
                println(t.message)
                println(dietRecords[0])
            }
        })
    }

    private fun addDietRecordResponse(response: Response<DietRecord>) {
        if (response.isSuccessful) {
            val dietRecord: DietRecord? = response.body()
            if (dietRecord != null) {
                when (response.code()) {
                    200 -> {
                        showToast("新增完成")
                        dietRecords.clear()
                        finish()
                    }
                    404 -> showToast("404 錯誤")
                    else -> showToast("伺服器故障")
                }
            } else {
                showToast("新增紀錄失敗")
                println(response.toString())
            }
        } else {
            showToast("新增紀錄請求失敗：" + response.message())
            println(response.toString())
        }
        dismissProgressDialog()
    }

    private fun displayFoodList() {
        foodOptionAdapter = FoodOptionAdapter(this, dietRecords)
        listView.adapter = foodOptionAdapter
        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                deleteRecordDialog(dietRecords[position], position)
                true
            }
    }

    private fun deleteRecordDialog(dietRecordData: RetrofitAPI.DietRecordData, id: Int) {
        val builder = AlertDialog.Builder(this@FoodOptionsActivity)
        builder.setTitle("刪除食物")

        val view = layoutInflater.inflate(R.layout.dialog_delete_custom_food, null)
        builder.setView(view)

        val isDelete = view.findViewById<TextView>(R.id.delete_custom_food)
        val foodName = dietRecordData.name
        isDelete.text = "是否刪除「$foodName」？"

        builder.setPositiveButton("刪除") { dialog, _ ->
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

    private fun showButtonTime(label: String) {
        when (label) {
            "早餐" -> timeButton.text = "08:00"
            "午餐" -> timeButton.text = "12:00"
            "晚餐" -> timeButton.text = "18:00"
            "點心/其他" -> timeButton.text = "15:00"
        }
    }

    private fun showDateText(dateText: String): String {
        var data = dateText.split(' ')
        var date = data[0]
        var dateData = date.split('-')
        var year = dateData[0]
        var month = dateData[1]
        var day = dateData[2]
        return (year + "年" + month + "月" + day + "日")
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