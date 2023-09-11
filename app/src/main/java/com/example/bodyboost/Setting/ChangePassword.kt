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
import com.example.bodyboost.Setting.EditFragment
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword: AppCompatActivity() {
    private lateinit var new_pwd:EditText
    private lateinit var check_pwd:EditText
    private lateinit var back_btn: Button
    private val currentUser = UserSingleton.user
    private val retrofitAPI = RetrofitManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepwd)

        new_pwd = findViewById(R.id.new_pwd)
        check_pwd = findViewById(R.id.check_pwd)
        back_btn = findViewById(R.id.back)
        if(new_pwd.text != null && check_pwd.text != null){
            if(new_pwd == check_pwd){
                currentUser?.let { getCurrentUsers(it.id) }
            }
        }
        back_btn.setOnClickListener {

        }
    }


    private fun getCurrentUsers(userId: Int) {
        retrofitAPI.getUsers(userId).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                when (response.code()) {
                    200 -> {
                        val currentUsers = response.body()
                        if (currentUsers != null) {
                            currentUsers.password = new_pwd.toString()
                            updatePwd(currentUsers)
                        }
                    }
                    400 -> showToast("格式錯誤")
                    404 -> showToast("查無此資料")
                    else -> showToast("伺服器故障: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                showToast("伺服器故障")
            }
        })
    }

    private fun updatePwd(user: Users) {
        val userId = currentUser?.id
        if (userId != null) {
            retrofitAPI.updatePwd(userId, user).enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    when (response.code()) {
                        200 -> {
                            showToast("修改密碼成功")
                            replaceFragment(EditFragment())
                        }
                        400 -> showToast("格式錯誤")
                        404 -> showToast("查無此資料")
                        else -> showToast("伺服器故障: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    showToast("伺服器故障")
                }
            })
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
    }
}