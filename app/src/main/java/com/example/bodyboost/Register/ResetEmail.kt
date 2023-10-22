package com.example.bodyboost.Register
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.Model.Users
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitManager
import com.example.bodyboost.UserSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetEmail: AppCompatActivity(){
    private val retrofitAPI = RetrofitManager.getInstance()
    private val currentUser = UserSingleton.user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_resetemail)
        val enterNewMail = findViewById<EditText>(R.id.new_email)
        val reset_mail_btn = findViewById<Button>(R.id.reset_mail_btn)
        reset_mail_btn.setOnClickListener{
            val mail = enterNewMail.text.toString()


            if(mail.isEmpty()){
                showToast("請確認已填寫電子信箱")
            }else{
                val userId = currentUser?.id
                if(userId!=null){
                    getCurrentUser(userId,mail)
                }
            }
        }
    }
    private fun getCurrentUser(userId:Int,mail:String){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("正在加载...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        currentUser?.id?.let { userId ->
            retrofitAPI.getUsers(userId).enqueue(object : Callback<Users> {
                override fun onResponse(call: Call<Users>, response: Response<Users>) {
                    when (response.code()) {
                        200 -> {
                            val currentUser = response.body()
                            if (currentUser  != null) {
                                currentUser.email = mail.toString()
                                updateEmail(currentUser)
                            }
                        }

                        400 -> {
                            showToast("電子郵件錯誤")
                        }

                        404 -> {
                            // 404: 驗證碼未找到
                            showToast("找不到電子郵件")
                            // 可以執行額外的處理程序，例如提示重新驗證
                        }

                        else -> {
                            // 其他未處理的狀態碼
                            showToast("未知錯誤：${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<Users>, t: Throwable) {
                    showToast("請求失敗：${t.message}")
                }
            })
        }
    }
    private fun updateEmail(user: Users) {
        val userId = currentUser?.id
        if (userId != null) {
            retrofitAPI.updateEmail(userId, user).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    when (response.code()) {
                        200 -> {
                            showToast("電子郵件更新成功")
                            startVerificationCodeActivity()
                        }
                        400 -> showToast("電子郵件更新失敗")
                        404 -> showToast("查無此資料")
                        else -> showToast("伺服器故障: ${response.message()}")
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
    private fun startVerificationCodeActivity() {
        startActivity(Intent(this, VerificationCode::class.java))
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }
}