package com.example.bodyboost.Register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.Model.Users
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitAPI
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationCode : AppCompatActivity() {
    private val retrofitAPI = RetrofitManager.getInstance()
    private val currentUser = UserSingleton.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_code)
        val skip_btn = findViewById<TextView>(R.id.skip)
        skip_btn.setOnClickListener {
            val intent = Intent(this@VerificationCode, RegisterInfoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }
        //-----------返回按鈕-------------
        val BackButton = findViewById<View>(R.id.back) as Button
        BackButton.setOnClickListener {
            val intent = Intent(this@VerificationCode, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        }

        val enterVerifyNum = findViewById<EditText>(R.id.verify_num)
        val verifyButton = findViewById<Button>(R.id.verify)
        verifyButton.setOnClickListener {
            currentUser?.id?.let { userId ->
                val code = enterVerifyNum.text.toString()
                val registerNumData = RetrofitAPI.RegisterNumData(code, userId)
                retrofitAPI.testRegisterNum(registerNumData).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        when (response.code()) {
                            200 -> {
                                // 200: 驗證成功
                                showToast("驗證成功")
                                startRegisterInfoActivity()
                            }
                            400 -> {
                                // 400: 驗證失敗或驗證碼過期
                                val responseBody = response.errorBody()?.string()
                                if (responseBody?.contains("Verified failed") == true) {
                                    showToast("驗證失敗")
                                    showFailedVerificationDialog()
                                    enterVerifyNum.setText(null)
                                } else if (responseBody?.contains("Verification code has expired") == true) {
                                    showToast("驗證碼已過期")
                                    showExpiredVerificationDialog()
                                    enterVerifyNum.setText(null)
                                    // 可以執行額外的處理程序，例如提示重新驗證
                                }
                            }
                            404 -> {
                                // 404: 驗證碼未找到
                                showToast("找不到驗證碼")
                                // 可以執行額外的處理程序，例如提示重新驗證
                            }
                            else -> {
                                // 其他未處理的狀態碼
                                showToast("未知錯誤：${response.code()}")
                            }
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        showToast("請求失敗：${t.message}")
                    }
                })
            }
        }
    }

    private fun showFailedVerificationDialog() {
        val account = UserSingleton.user
        val mail = UserSingleton.user?.email
        if (account != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("驗證碼錯誤")
                .setMessage("請確認電子郵件： $mail 輸入正確，確認後請重新驗證")
                .setPositiveButton("重新驗證") { _, _ ->
                    // 重新驗證按鈕
                    resendVerificationMail(account)
                }
                .setNegativeButton("修改電子郵件") { _, _ ->
                    startResetEmailActivity()
                }
            builder.create().show()
        } else {
            // 處理 currentUser 為空的情況
            showToast("無法獲取帳號資訊")
        }
    }
    private fun showExpiredVerificationDialog() {
        val account = UserSingleton.user
        val mail = UserSingleton.user?.email
        if (account != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("驗證碼過期")
                .setMessage("請確認電子郵件： $mail 輸入正確，確認後請重新驗證")
                .setPositiveButton("重新驗證") { _, _ ->
                    // 重新驗證按鈕
                    resendVerificationMail(account)
                }
                .setNegativeButton("修改電子郵件") { _, _ ->
                    startResetEmailActivity()
                }
            builder.create().show()
        } else {
            // 處理 currentUser 為空的情況
            showToast("無法獲取帳號資訊")
        }
    }
    private fun resendVerificationMail(account: Users) {
        Log.d("Debug", "Attempting to resend verification mail for account: $account")
        retrofitAPI.resendVerificationMail(account).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                Log.d("Debug", "API onResponse: ${response.code()}")
                if (response.isSuccessful) {
                    when (response.code()) {
                        200 -> {
                            // 200: 驗證碼重新寄送成功
                            showToast("驗證碼郵件重新寄送成功")
                        }
                        404 -> {
                            // 404: 驗證碼郵件寄送失敗
                            showToast("驗證碼郵件重新寄送失敗")
                            showFailedVerificationDialog()
                            // 可以執行額外的處理程序，例如提示重新驗證
                        }
                        else -> {
                            // 其他未處理的狀態碼
                            showToast("未知錯誤：${response.code()}")
                        }
                    }
                } else {
                    // 非 2xx 狀態碼，請求失敗
                    showToast("請求失敗：${response.code()}")
                }
            }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                showToast("伺服器故障")
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startRegisterInfoActivity() {
        val intent = Intent(this@VerificationCode, RegisterInfoActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close this activity if needed
    }

    fun showAlertDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        positiveButtonAction: () -> Unit,
        negativeButtonText: String,
        negativeButtonAction: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this@VerificationCode)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                positiveButtonAction.invoke()
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                negativeButtonAction.invoke()
            }
        builder.create().show()
    }
    private fun startRegisterActivity() {
        val intent = Intent(this@VerificationCode, RegisterActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close this activity if needed
    }
    private fun startResetEmailActivity() {
        val intent = Intent(this@VerificationCode, ResetEmail::class.java)
        startActivity(intent)
        finish() // Optional: Close this activity if needed
    }
}