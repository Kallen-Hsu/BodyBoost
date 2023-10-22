package com.example.bodyboost.Setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.bodyboost.R

class SettingFragment : Fragment()  {
    private lateinit var notification_btn: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var dark_mode:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        sharedPref = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        if (isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        dark_mode=view.findViewById(R.id.dark_mode)
        dark_mode.setOnClickListener {
            if (isNightModeEnabled()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setNightModeEnabled(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setNightModeEnabled(true)
            }
            requireActivity().recreate() // 重新創建Activity以應用新的主題
        }

        notification_btn = view.findViewById<Button>(R.id.notification_btn)
        notification_btn.setOnClickListener{
            val fragment = NotificationFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
        return view
    }
    private fun isNightModeEnabled(): Boolean {
        return sharedPref.getBoolean("night_mode", false)
    }

    private fun setNightModeEnabled(enabled: Boolean) {
        sharedPref.edit().putBoolean("night_mode", enabled).apply()
    }
}