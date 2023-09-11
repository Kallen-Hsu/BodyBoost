package com.example.bodyboost.Register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.LoginActivity
import com.example.bodyboost.Model.Users
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitAPI
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    private val retrofitAPI = RetrofitManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val enterAcc = findViewById<EditText>(R.id.username)
        val enterPwd = findViewById<EditText>(R.id.password)
        val enterCheckPwd = findViewById<EditText>(R.id.check_password)
        val enterEmail = findViewById<EditText>(R.id.email)

        //-----------下一步按鈕-------------
        val registerButton = findViewById<Button>(R.id.register)
        registerButton.setOnClickListener {
            val account = enterAcc.text.toString()
            val password = enterPwd.text.toString()
            val checkedPassword = enterCheckPwd.text.toString()
            val email = enterEmail.text.toString()
            when {
                password != checkedPassword -> showToast("密碼輸入錯誤")
                account.isEmpty() || password.isEmpty() || checkedPassword.isEmpty() || email.isEmpty() -> showToast("請確認所有欄位皆已填")
                else -> addUser(account, password, email)
            }
        }
        //-----------返回按鈕-------------
        val BackButton = findViewById<View>(R.id.back) as Button
        BackButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
    }
    private fun addUser(account: String, password: String, email: String) {
        val addUserData = RetrofitAPI.AddUserData(account, password, email)
        retrofitAPI.addUser(addUserData).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                handleAddUserResponse(response)
            }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                val toast = Toast(applicationContext)
                toast.setText("請求失敗：" + t.message)
                t.printStackTrace() // 印出錯誤堆疊以便排查問題
                dismissProgressDialogAndShowToast(toast)
            }
        })
    }
    private fun handleAddUserResponse(response: Response<Users>) {
        val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
        if (response.isSuccessful) {
            val users: Users? = response.body()
            if (users != null) {
                when (response.code()) {
                    200 -> {
                        // 200: 驗證碼寄送成功
                        UserSingleton.user = users
                        toast.setText("完成")
                        startRegisterInfoActivity()
                    }
                    400 -> {
                        // 400: 帳號密碼已存在
                        showToast("帳號密碼已存在")
                    }

                    else -> {
                        // 其他未處理的狀態碼
                        showToast("未知錯誤：${response.code()}")
                    }
                }
            }
        }
        dismissProgressDialogAndShowToast(toast)
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
    private fun dismissProgressDialogAndShowToast(toast: Toast) {
        progressDialog?.dismiss()
        toast.show()
    }
    private fun startRegisterInfoActivity() {
        val intent = Intent(this@RegisterActivity, VerificationCode::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }
}