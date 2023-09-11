package com.example.bodyboost.Register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.MainActivity
import com.example.bodyboost.Model.Profile
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Target1 : AppCompatActivity() {
    private val retrofitAPI = RetrofitManager.getInstance()
    private val currentUser = UserSingleton.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.target_weight)

        val weightEditText = findViewById<EditText>(R.id.weight)
        val targetWeightEditText = findViewById<EditText>(R.id.target_weight)
        val targetButton = findViewById<Button>(R.id.finish)

        targetButton.setOnClickListener {
            val weightText = weightEditText.text.toString()
            val targetWeightText = targetWeightEditText.text.toString()

            if (weightText.isEmpty() || targetWeightText.isEmpty()) {
                showToast("請確認所有欄位皆以填寫")
            } else {
                val userId = currentUser?.id
                if (userId != null) {
                    getCurrentProfile(userId, weightText, targetWeightText)
                }
            }
        }
    }

    private fun getCurrentProfile(userId: Int, weightText: String, targetWeightText: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("正在加载...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        retrofitAPI.getProfile(userId).enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                progressDialog.dismiss()

                when (response.code()) {
                    200 -> {
                        val currentProfile = response.body()
                        if (currentProfile != null) {
                            // 更新體重和目標體重
                            currentProfile.weight = weightText.toDouble()
                            currentProfile.weight_goal = targetWeightText.toDouble()
                            updateWeight(currentProfile)
                        }
                    }
                    400 -> showToast("格式錯誤")
                    404 -> showToast("查無此資料")
                    else -> showToast("伺服器故障: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                progressDialog.dismiss()
                showToast("伺服器故障")
            }
        })
    }

    private fun updateWeight(profile: Profile) {
        val userId = currentUser?.id
        if (userId != null) {
            retrofitAPI.update_weight(userId, profile).enqueue(object : Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    when (response.code()) {
                        200 -> {
                            showToast("歡迎加入BodyBoost")
                            startMainActivity()
                        }
                        400 -> showToast("格式錯誤")
                        404 -> showToast("查無此資料")
                        else -> showToast("伺服器故障: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    showToast("伺服器故障")
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }
}


