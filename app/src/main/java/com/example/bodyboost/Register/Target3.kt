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

class Target3 : AppCompatActivity() {
    private val retrofitAPI = RetrofitManager.getInstance()
    private val currentUser = UserSingleton.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.target_muscle)

        val fatEditText = findViewById<EditText>(R.id.fat)
        val targetButton = findViewById<Button>(R.id.finish)

        targetButton.setOnClickListener {
            val fatText = fatEditText.text.toString()

            if (fatText.isEmpty()) {
                showToast("請確認所有欄位皆以填寫")
            } else {
                val userId = currentUser?.id
                if (userId != null) {
                    getCurrentProfile(userId, fatText)
                }
            }
        }
    }

    private fun getCurrentProfile(userId: Int, fatText: String) {
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
                            // 更新體脂率
                            currentProfile.body_fat = fatText.toDouble()
                            updateFat(currentProfile)
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

    private fun updateFat(profile: Profile) {
        val userId = currentUser?.id
        if (userId != null) {
            retrofitAPI.update_fat(userId, profile).enqueue(object : Callback<Profile> {
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

