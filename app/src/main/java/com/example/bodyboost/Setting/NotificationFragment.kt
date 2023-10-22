package com.example.bodyboost.Setting
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.bodyboost.R
import com.example.bodyboost.ReminderBroadcastReceiver
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import java.util.Calendar

class NotificationFragment : Fragment() {

    private lateinit var addNotificationTimeBtn: Button
    private lateinit var notificationSwitch: Switch
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 123
    private val retrofitAPI = RetrofitManager.getInstance()
    private var isAlerted = false
    private val currentUser = UserSingleton.user
    private val animCharName = "normal"
    private val theme = "light"
    private val calendar = Calendar.getInstance()
    private val selectedDaysOfWeek = mutableSetOf<Int>() // 使用 Set 以确保不重复的选择
    private val daysOfWeek = arrayOf(
        "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.notification, container, false)

        // 檢查並請求通知權限
        checkAndRequestNotificationPermission()
        addNotificationTimeBtn = view.findViewById(R.id.selectTimeButton)
        addNotificationTimeBtn.setOnClickListener {
            showTimePicker()
        }

        notificationSwitch = view.findViewById(R.id.notification_switch)
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            isAlerted = isChecked
        }

        return view
    }

    private fun showTimePicker() {
        Log.d("NotificationFragment", "showTimePicker() called")
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                showConfirmationDialog(selectedTime)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun showConfirmationDialog(selectedTime: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("請確認選擇時間")
        builder.setMessage("您選擇的時間是: $selectedTime, 確定嗎?")

        builder.setPositiveButton("確定") { _, _ ->
            addNotificationTimeBtn.text = selectedTime
            showDayOfWeekSelectionDialog()
            // updateSetting(theme, animCharName, true, alertDay, selectedTime)
        }

        builder.setNegativeButton("取消") { _, _ ->

        }

        builder.show()
    }

    private fun showDayOfWeekSelectionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("選擇提醒")

        val checkedItems = BooleanArray(7) { selectedDaysOfWeek.contains(it) }
        builder.setMultiChoiceItems(daysOfWeek, checkedItems) { _, which, isChecked ->
            if (isChecked) {
                selectedDaysOfWeek.add(which)
            } else {
                selectedDaysOfWeek.remove(which)
            }
        }

        builder.setPositiveButton("確定") { _, _ ->
            if (selectedDaysOfWeek.isNotEmpty()) {
                val selectedDaysText = selectedDaysOfWeek.joinToString(", ") { daysOfWeek[it] }
                setWeeklyReminder(selectedDaysOfWeek.toList())
                showToast(
                    "設定提醒 $selectedDaysText",
                    requireContext()
                )
            } else {
                showToast("請至少選擇一天提醒", requireContext())
            }
        }

        builder.setNegativeButton("取消") { _, _ ->

        }

        builder.show()
    }

    private fun setWeeklyReminder(selectedDaysOfWeek: List<Int>) {
        val intent = Intent(requireContext(), ReminderBroadcastReceiver::class.java)
        val requestCode = 0
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =
            requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val triggerTimeMillis = calendar.timeInMillis
        val intervalMillis = AlarmManager.INTERVAL_DAY * 7

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            intervalMillis,
            pendingIntent
        )
    }

    private fun checkAndRequestNotificationPermission() {
        if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
            showNotificationPermissionDialog()
        } else {
            //UpdateSetting(theme, animCharName, isAlerted, alertDay, alertTime)
        }
    }

    private fun showNotificationPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("獲取通知權限")
        builder.setMessage("您需要給予APP通知權限")

        builder.setPositiveButton("前往設定") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE)
        }
        builder.setNegativeButton("取消") { _, _ ->
        }
        builder.show()
    }

    private fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}