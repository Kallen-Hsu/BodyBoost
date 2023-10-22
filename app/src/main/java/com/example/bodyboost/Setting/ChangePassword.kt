package com.exampl

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bodyboost.Model.Users
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : AppCompatActivity() {
    private lateinit var new_pwd: EditText
    private lateinit var check_pwd: EditText
    private lateinit var back_btn: Button
    private lateinit var check_btn: Button
    private val currentUser = UserSingleton.user
    private val retrofitAPI = RetrofitManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepwd)

        new_pwd = findViewById(R.id.new_pwd)
        check_pwd = findViewById(R.id.check_pwd)
        check_btn = findViewById(R.id.check_btn)
        back_btn = findViewById(R.id.back)

        check_btn.setOnClickListener {
            val newPassword = new_pwd.text.toString()
            val confirmPassword = check_pwd.text.toString()

            if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword == confirmPassword) {
                currentUser?.let { getCurrentUser(it.id, newPassword) }
            } else {
                showToast("請確認密碼輸入正確並不為空")
            }
        }

        back_btn.setOnClickListener {
            finish()
        }
    }

    private fun getCurrentUser(userId: Int, newPassword: String) {
        retrofitAPI.getUsers(userId).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val currentUsers = response.body()
                    currentUsers?.let {
                        it.password = newPassword
                        updatePassword(it)
                    }
                } else {
                    showToast("伺服器故障: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                showToast("伺服器故障")
            }
        })
    }

    private fun updatePassword(user: Users) {
        val userId = currentUser?.id
        userId?.let {
            retrofitAPI.updatePwd(it, user).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showToast("修改密碼成功")
                        finish()
                    } else {
                        showToast("伺服器故障: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showToast("伺服器故障")
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}