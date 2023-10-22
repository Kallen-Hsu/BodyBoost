package com.example.bodyboost

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Notification: Fragment() {

    private lateinit var selectWeeklyTimeBtn: Button
    private val calendar = Calendar.getInstance()
    private val selectedDaysOfWeek = ArrayList<Int>() // 存儲所選星期的清單
    private val daysOfWeek = arrayOf(
        "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    )
    private val CHANNEL_ID = "Your_Channel_ID" // 請自行指定通知渠道的 ID

    private val MY_PERMISSIONS_REQUEST_SEND_NOTIFICATIONS = 123 // 權限請求碼

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 權限已授予，執行相應的操作，例如顯示時間選擇器
            showTimePicker()
        } else {
            // 權限未被授予，您可以顯示一條消息給用戶或採取其他操作
            showToast("您需要授予通知權限才能使用此功能", requireContext())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.test, container, false)

        selectWeeklyTimeBtn = view.findViewById(R.id.selectTimeButton)
        selectWeeklyTimeBtn.setOnClickListener {
            // 檢查權限並啟動時間選擇器
            checkAndRequestNotificationPermission()
        }

        return view
    }

    private fun showTimePicker() {
        // 時間選擇器的代碼不變
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // 設定選擇的時間
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val selectedTime = dateFormat.format(calendar.time)
                showDayOfWeekSelectionDialog(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        timePickerDialog.show()
    }

    private fun showDayOfWeekSelectionDialog(selectedTime: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("選擇提醒的星期")

        // 使用多選列表對話框
        val checkedItems = BooleanArray(7) // 預設都為 false
        builder.setMultiChoiceItems(daysOfWeek, checkedItems) { _, which, isChecked ->
            if (isChecked) {
                selectedDaysOfWeek.add(which)
            } else {
                selectedDaysOfWeek.remove(which)
            }
        }

        builder.setPositiveButton("確定") { _, _ ->
            // 在此處設定每週提醒，使用 selectedTime 和 selectedDaysOfWeek
            // 例如，可以設定一個定時任務以在所選星期的選擇時間觸發提醒
            // 記得處理提醒相關的邏輯
            if (selectedDaysOfWeek.isNotEmpty()) {
                val selectedDaysText = selectedDaysOfWeek.joinToString(", ") { daysOfWeek[it] }
                setWeeklyReminder(selectedTime, selectedDaysOfWeek)
                showToast("已設定每週${selectedDaysText}的提醒時間：$selectedTime", requireContext())
            } else {
                showToast("請至少選擇一天作為提醒的星期", requireContext())
            }
        }

        builder.setNegativeButton("取消") { _, _ ->
            // 使用者選擇取消，不執行任何操作
        }

        builder.show()
    }

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setWeeklyReminder(selectedTime: String, selectedDaysOfWeek: List<Int>) {
        // 設定提醒的邏輯，使用 AlarmManager

        val intent = Intent("com.example.bodyboost.REMINDER_ACTION")
        // 在這裡可以添加額外的資料到 intent，以便在 BroadcastReceiver 中處理

        val requestCode = 0 // 指定一個獨一無二的 requestCode
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 設定提醒，這個示例中使用的是重複提醒
        val triggerTimeMillis = calendar.timeInMillis
        val intervalMillis = AlarmManager.INTERVAL_DAY * 7 // 每週觸發一次

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            intervalMillis,
            pendingIntent
        )

        // 發送通知
        sendNotification(selectedTime)
    }

    private fun sendNotification(selectedTime: String) {
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("每週提醒")
            .setContentText("已設定每週提醒時間：$selectedTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            notificationManager.notify(1, builder.build())
        }
    }

    private fun checkAndRequestNotificationPermission() {
        // 檢查是否已獲得通知權限
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果未獲得權限，請進行權限請求
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // 已獲得權限，顯示時間選擇器
            showTimePicker()
        }
    }
}
