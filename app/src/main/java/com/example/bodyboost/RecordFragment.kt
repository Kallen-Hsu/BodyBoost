package com.example.bodyboost

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bodyboost.Food.FoodRecordAdapter
import com.example.bodyboost.Model.DietRecord
import com.example.bodyboost.Food.SearchFoodActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RecordFragment : Fragment() {

    val currentUser = UserSingleton.user
    private var userId: Int = 0
    private val retrofitAPI = RetrofitManager.getInstance()
    private var progressDialog: ProgressDialog? = null

    private var dietRecords: List<DietRecord>? = null
    private lateinit var foodRecordAdapter: FoodRecordAdapter

    private lateinit var calendarButton: Button
    private lateinit var waterButton: ImageButton
    private lateinit var breakfastButton: ImageButton
    private lateinit var lunchButton: ImageButton
    private lateinit var dinnerButton: ImageButton
    private lateinit var otherFoodButton: ImageButton
    private lateinit var dateTextView: TextView
    private lateinit var listViewBreakfast: ListView
    private lateinit var listViewLunch: ListView
    private lateinit var listViewDinner: ListView
    private lateinit var listViewOther: ListView

    // food list
    private val foodRecordList = mutableListOf<String>()
    private val dateRecordList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_record, container, false)

        // findViewById
        calendarButton = rootView.findViewById(R.id.button_calendar)
        dateTextView = rootView.findViewById(R.id.textView_date)
        breakfastButton = rootView.findViewById(R.id.button_addBreakfast)
        lunchButton = rootView.findViewById(R.id.button_addLunch)
        dinnerButton = rootView.findViewById(R.id.button_addDinner)
        otherFoodButton = rootView.findViewById(R.id.button_addOther)
        waterButton = rootView.findViewById(R.id.button_addWater)

        // show date
        val dateFormat = SimpleDateFormat("yyyy年 MM月 dd日", Locale.getDefault())
        dateTextView.text = dateFormat.format(Date())
        // date text
        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        FoodListSingleton.dateText = dateFormat2.format(Date())
        calendarButton.setOnClickListener {
            // select date
            showDatePicker(dateTextView)
        }

        breakfastButton.setOnClickListener {
            FoodListSingleton.label = "早餐"
            replaceActivity()
        }

        lunchButton.setOnClickListener {
            FoodListSingleton.label = "午餐"
            replaceActivity()
        }

        dinnerButton.setOnClickListener {
            FoodListSingleton.label = "晚餐"
            replaceActivity()
        }

        otherFoodButton.setOnClickListener {
            FoodListSingleton.label = "點心/其他"
            replaceActivity()
        }

        waterButton.setOnClickListener {
            showWaterDialog()
        }

        // Inflate the layout for this fragment
        return rootView
    }

    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _: DatePicker, y: Int, m: Int, d: Int ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(y, m, d)

                // 使用 SimpleDateFormat 將日期格式化為 'YYYY年 MM月 DD日' 的字串
                val dateFormat = SimpleDateFormat("yyyy年 MM月 dd日", Locale.getDefault())
                textView.text = dateFormat.format(selectedCalendar.time)
                // save date text
                val dateFormat2 = SimpleDateFormat("yyyy-MM-dd ", Locale.getDefault())
                FoodListSingleton.dateText = dateFormat2.format(selectedCalendar.time)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showWaterDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("記錄飲水量")

        val view = layoutInflater.inflate(R.layout.dialog_water, null)
        builder.setView(view)

        val waterDate = view.findViewById<TextView>(R.id.dateTextView)
        val waterText = view.findViewById<EditText>(R.id.waterEditText)
        waterDate.text = dateTextView.text
        waterDate.setOnClickListener {
            showDatePicker(waterDate)
        }

        builder.setPositiveButton("完成") { dialog, _ ->
            val selectedDate = waterDate.text.toString()
            val inputNumber = waterText.text.toString()
            Toast.makeText(this.context, "往健康水美人邁進 $inputNumber 步", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("取消", null)

        val dialog = builder.create()
        dialog.show()
    }

    private fun displayDietRecord(userID: Int) {
        loadProgressDialog()
        retrofitAPI.getDietRecord(userID.toString()).enqueue(object : Callback<List<DietRecord>> {
            override fun onResponse(call: Call<List<DietRecord>>, response: Response<List<DietRecord>>) {
                displayDietRecordResponse(response)
            }
            override fun onFailure(call: Call<List<DietRecord>>, t: Throwable) {
                showToast("請求失敗：" + t.message)
                t.printStackTrace()
                dismissProgressDialog()
                println(t.message)
            }
        })
    }

    private fun displayDietRecordResponse(response: Response<List<DietRecord>>) {
        if (response.isSuccessful) {
            val dietRecord: List<DietRecord>? = response.body()
            if (dietRecord != null) {
                when (response.code()) {
                    200 -> {
                        this.dietRecords = dietRecord
                        foodRecordAdapter = FoodRecordAdapter(this, dietRecords!!)
                        foodListView(foodRecordAdapter, listViewBreakfast)
                    }
                    404 -> showToast("404 錯誤")
                    else -> showToast("伺服器錯誤，請稍後再試")
                }
            } else {
                println(response.toString())
            }
        } else {
            println(response.toString())
        }
        dismissProgressDialog()
    }

    private fun foodListView(foodRecordAdapter: FoodRecordAdapter, listView: ListView) {
        listView.adapter = foodRecordAdapter
    }

    private fun replaceActivity() {
        val intent = Intent(activity, SearchFoodActivity::class.java)
        startActivity(intent)
    }

    private fun loadProgressDialog() {
        progressDialog = ProgressDialog(this.context).apply {
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
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

}