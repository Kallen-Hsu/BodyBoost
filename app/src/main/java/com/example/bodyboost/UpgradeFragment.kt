package com.example.bodyboost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bodyboost.Model.Member
import com.example.bodyboost.Model.Users
import com.example.bodyboost.Setting.EditFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpgradeFragment : Fragment() {
    private lateinit var month_upgrade_btn: Button
    private lateinit var year_upgrade_btn: Button
    private val currentUser = UserSingleton.user
    private val retrofitAPI = RetrofitManager.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upgrade, container, false)
        month_upgrade_btn = view.findViewById<Button>(R.id.month)
        month_upgrade_btn.setOnClickListener {
            val member_type = "normal"
            //val phone =
            val is_trial = true
            val payment_type = "year"
        }
        year_upgrade_btn = view.findViewById<Button>(R.id.year)
        year_upgrade_btn.setOnClickListener {

        }
        return view
    }
//private fun upgrade(userId: Int) {
//    val userId = currentUser?.id
//    val memberData = RetrofitAPI.MemberData(member_type,phone,is_trial,payment_type)
//    if (userId != null) {
//        retrofitAPI.Upgrade(userId,memberData).enqueue(object : Callback<Member> {
//            override fun onResponse(call: Call<Member>, response: Response<Member>) {
//                when (response.code()) {
//                    200 -> {
//                        showToast("成功升級")
//                    }
//                    400 -> showToast("格式錯誤")
//                    404 -> showToast("查無此資料")
//                    else -> showToast("伺服器故障: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<Member>, t: Throwable) {
//                showToast("伺服器故障")
//            }
//        })
//    }
//    })
//}
//    private fun showToast(message: String) {
//        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
//    }
}


