@file:Suppress("DEPRECATION", "SameParameterValue")

package com.example.bodyboost
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodyboost.Model.Users
import com.example.bodyboost.Register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class LoginActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    private val retrofitAPI = RetrofitManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val enterAcc = findViewById<EditText>(R.id.enter_account)
        val enterPwd = findViewById<EditText>(R.id.enter_password)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgetPwdButton = findViewById<Button>(R.id.forgetpwd)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val account = enterAcc.text.toString()
            val password = enterPwd.text.toString()

            if (account.isNotEmpty() && password.isNotEmpty()) {
                loginNormal(account, password)
            } else {
                showToast("請確認所有欄位皆已填")
            }
        }

        forgetPwdButton.setOnClickListener {
            startForgetPasswordActivity()
        }

        registerButton.setOnClickListener {
            startRegisterActivity()
        }
    }

    private fun loginNormal(account: String, password: String) {
        loadProgressDialog()
        val loginData = RetrofitAPI.LoginData(account, password)
        val call: Call<Users> = retrofitAPI.loginNormal(loginData)
        call.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                handleLoginResponse(response)
            }
            override fun onFailure(call: Call<Users>, t: Throwable) {
                val toast = Toast(applicationContext)
                toast.setText("請求失敗：" + t.message)
                t.printStackTrace() // 印出錯誤堆疊以便排查問題
                dismissProgressDialogAndShowToast(toast)
            }
        })
    }

    private fun handleLoginResponse(response: Response<Users>) {
        val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
        if (response.isSuccessful) {
            val users: Users? = response.body()
            if (users != null) {
                when (response.code()) {
                    200 -> {
                        UserSingleton.user = users
                        toast.setText("登入成功")
                        navigateToMainActivity()
                    }
                    404 -> toast.setText("帳號或密碼錯誤")
                    else -> toast.setText("伺服器故障")
                }
            } else {
                toast.setText("伺服器返回的數據為空")
            }
        } else {
            toast.setText("帳號或密碼錯誤" + response.message())
        }
        dismissProgressDialogAndShowToast(toast)
    }


    private fun navigateToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        progressDialog?.dismiss()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun loadProgressDialog() {
        progressDialog = ProgressDialog(this@LoginActivity).apply {
            setCancelable(false)
            setMessage("Loading...")
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun dismissProgressDialogAndShowToast(toast: Toast) {
        progressDialog?.dismiss()
        toast.show()
    }

    private fun startForgetPasswordActivity() {
        val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }

    private fun startRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }
}










//    private late init var googleBtn: Button
//    private late init var oneTapClient: SignInClient
//    private late init var signInRequest: BeginSignInRequest
//    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
//    private var showOneTapUI = true


//        //----------google one tap------------
//        googleBtn = findViewById<View>(R.id.google_login)as Button
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setPasswordRequestOptions(
//                BeginSignInRequest.PasswordRequestOptions.builder()
//                    .setSupported(true)
//                    .build()
//            )
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.OneTap))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build()
//            )
//            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
//            .build()
//
//
//            val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    try {
//                        val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(result.data)
//                        val idToken: String? = credential.googleIdToken
//                        val username: String = credential.id
//                        val password: String? = credential.password
//
//                        if (idToken != null) {
//                            val email: String = credential.id
//                            Toast.makeText(applicationContext, "Email $email", Toast.LENGTH_SHORT).show()
//                        }
//                    } catch (e: ApiException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            googleBtn.setOnClickListener {
//                oneTapClient.beginSignIn(signInRequest)
//                    .addOnSuccessListener(this) {
//                            result ->
//                            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                            activityResultLauncher.launch(intentSenderRequest)
//
//                    }
//                    .addOnFailureListener(this) { e ->
//                        e.localizedMessage?.let { it1 -> Log.d("TAG", it1) }
//                    }
//            }
////            //----------google登入按鈕----------
////            fun loginGoogle(email:String){
////                loadEvent(this@LoginActivity)
////                retrofitAPI = RetrofitManager.getInstance().getAPI()
////
////            }