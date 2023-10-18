package com.example.bodyboost

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bodyboost.food.SearchFoodActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RecordFragment : Fragment() {

    private lateinit var calendarButton: Button
    private lateinit var waterButton: ImageButton
    private lateinit var breakfastButton: ImageButton
    private lateinit var lunchButton: ImageButton
    private lateinit var dinnerButton: ImageButton
    private lateinit var otherFoodButton: ImageButton
    private lateinit var dateTextView: TextView
    lateinit var dateText: String
    lateinit var label: String

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
        dateText = dateFormat2.format(Date())
        calendarButton.setOnClickListener {
            // select date
            showDatePicker(dateTextView)
        }

        breakfastButton.setOnClickListener {
            label = "早餐"
            replaceActivity()
        }

        lunchButton.setOnClickListener {
            label = "午餐"
            replaceActivity()
        }

        dinnerButton.setOnClickListener {
            label = "晚餐"
            replaceActivity()
        }

        otherFoodButton.setOnClickListener {
            label = "點心/其他"
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
                val dateFormat2 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
                dateText = dateFormat2.format(selectedCalendar.time)
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

    private fun replaceActivity() {
        val intent = Intent(activity, SearchFoodActivity::class.java)
        startActivity(intent)
    }


}