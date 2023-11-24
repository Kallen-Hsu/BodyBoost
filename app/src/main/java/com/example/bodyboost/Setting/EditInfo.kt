package com.example.bodyboost.Setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.Model.Profile
import com.example.bodyboost.ProfileSingleton
import com.example.bodyboost.R
import com.example.bodyboost.Register.VerificationCode
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditInfo : AppCompatActivity() {

    private val currentUser = UserSingleton.user
    private val profile = ProfileSingleton.profile
    private val retrofitAPI = RetrofitManager.getInstance()

    private lateinit var name: TextView
    private lateinit var gender: TextView
    private lateinit var height: TextView
    private lateinit var weight: EditText
    private lateinit var birthday: TextView
    private lateinit var backBtn: Button
    private lateinit var checkBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_info)

        name = findViewById(R.id.name)
        gender = findViewById(R.id.gender)
        height = findViewById(R.id.height)
        weight = findViewById(R.id.weight)
        birthday = findViewById(R.id.birthday)
        backBtn = findViewById(R.id.back)
        checkBtn = findViewById(R.id.check_btn)

        if (currentUser != null) {
            getProfile(currentUser.id)
        }

        backBtn.setOnClickListener {
            finish()
        }

        checkBtn.setOnClickListener {
            val newWeight = weight.text.toString()
            if (newWeight.isNotEmpty()) {
                getWeight(currentUser?.id ?: 0, newWeight.toDouble())
            } else {
                showToast("請確認體重並不為空")
            }
        }
    }

    private fun getProfile(userId: Int) {
        retrofitAPI.getProfile(userId).enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    profile?.let {
                        updateUIWithProfile(it)
                    }
                } else {
                    showToast("伺服器故障: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                showToast("伺服器故障")
            }
        })
    }

    private fun getWeight(userId: Int, newWeight: Number) {
        retrofitAPI.getUserProfile(userId).enqueue(object : Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    profile?.let {
                        it.weight = newWeight
                        updateWeight(it)
                    }
                } else {
                    showToast("伺服器故障: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Profile>, t: Throwable) {
                showToast("伺服器故障")
            }
        })
    }

    private fun updateWeight(profile: Profile) {
        val userId = currentUser?.id
        userId?.let {
            retrofitAPI.update_weight(it, profile).enqueue(object : Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    when (response.code()) {
                        200 -> {
                            showToast("體重更新成功")
                            startVerificationCodeActivity()
                        }
                        400 -> showToast("錯誤")
                        404 -> {
                            showToast("找不到資料")
                            // 可以執行額外的處理程序，例如提示重新驗證
                        }
                        else -> showToast("未知錯誤：${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    showToast("請求失敗：${t.message}")
                }
            })
        }
    }

    private fun updateUIWithProfile(profile: Profile) {
        name.text = profile.name
        gender.text = if (profile.gender == 2) "女" else "男"
        height.text = profile.height.toString()
        weight.hint = profile.weight.toString()
        birthday.text = profile.birthday
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun startVerificationCodeActivity() {
        startActivity(Intent(this, EditInfo::class.java))
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }
}