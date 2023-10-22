package com.example.bodyboost
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bodyboost.Manager.SignInManager
import com.example.bodyboost.Model.DailyBonus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val retrofitAPI = RetrofitManager.getInstance()
    private lateinit var check_in_btn: Button
    private val currentUser = UserSingleton.user
    private lateinit var signInManager: SignInManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        signInManager = SignInManager(requireContext())
        check_in_btn = view.findViewById<Button>(R.id.daily_signup)
        check_in_btn.setOnClickListener {
            if (signInManager.canSignIn()) {
                val userId = currentUser?.id
                val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                val currentTime = sdf.format(Date())
                if (userId != null) {
                    val dailyBonusData = RetrofitAPI.DailyBonusData(id, currentTime, userId)
                    retrofitAPI.addDailyBonus(userId, dailyBonusData).enqueue(object : Callback<DailyBonus> {
                        override fun onResponse(call: Call<DailyBonus>, response: Response<DailyBonus>) {
                            showToast("簽到成功", requireContext())
                            handleResponse(response)
                        }
                        override fun onFailure(call: Call<DailyBonus>, t: Throwable) {
                            showToast("簽到失敗: ${t.message}", requireContext())
                            t.printStackTrace()
                        }
                    })
                    // 執行簽到
                    signInManager.performSignIn()
                }
            } else {
                showToast("今天已經簽到過了！", requireContext())
            }
        }
        return view
    }

    private fun handleResponse(response: Response<DailyBonus>) {
        val toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)
        when (response.code()) {
            200 -> {
                showToast("簽到成功")
                navigateToMainActivity()
            }
            400 -> showToast("格式錯誤")
            404 -> showToast("查無此使用者")
            else -> showToast("伺服器故障: ${response.message()}")
        }
    }

    private fun showToast(message: String) {
        showToast(message, requireContext())
    }

    private fun showToast(message: String, context: Context) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }
    private fun navigateToMainActivity() {
        // 導航至主活動的程式碼
    }

}
