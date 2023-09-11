package com.example.bodyboost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bodyboost.Model.Profile

class AchievementFragment : Fragment() {
    private val retrofitAPI = RetrofitManager.getInstance()
    private lateinit var goal_08_txt: TextView
    private lateinit var goal_08_img: ImageView
    private lateinit var goal_09_txt: TextView
    private lateinit var goal_09_img: ImageView
    private val currentUser = UserSingleton.user
    private lateinit var profile: Profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achievement, container, false)
        goal_08_img = view.findViewById(R.id.goal_08_img)
        goal_08_txt = view.findViewById(R.id.goal_08_txt)
        goal_09_img = view.findViewById(R.id.goal_09_img)
        goal_09_txt = view.findViewById(R.id.goal_09_txt)

//        currentUser?.let { user ->
//            val goalHistory = getUserGoalHistory(user.id)
//            val weightHistory = getUserWeightHistory(user.id)
//            profile = getUserProfile(user.id)
//            val consecutiveMonths = checkConsecutiveMonths(goalHistory)
//            val lastMonthWeightLoss = calculateLastMonthWeightLoss(weightHistory)
//            if (consecutiveMonths && lastMonthWeightLoss >= 2.0 && profile.weight_goal.toDouble()<= weightHistory.last().weight.toDouble()) {
//                goal_08_txt.text = "瘦身達人: 恭喜您連續減重一個月，並減下 2 公斤以上，達成目標體重！"
//                goal_08_img.setImageResource(R.drawable.goal_08)
//            } else {
//
//            }
//            val consecutiveMonthsAchievement9 = checkConsecutiveMonthsAchievement9(goalHistory)
//            val totalWeightLoss = calculateTotalWeightLoss(weightHistory)
//
//            if (consecutiveMonthsAchievement9 && totalWeightLoss >= 10.0) {
//                goal_09_txt.text = "身材改造師: 恭喜您連續減重三個月，並減下 10 公斤以上！"
//                goal_09_img.setImageResource(R.drawable.goal_09)
//            }
//        }
        return view
    }

    // Implement the necessary functions to fetch data and calculate achievements

//    private fun getUserGoalHistory(user_id: Int): List<GoalHistory> {
//        val progressDialog = ProgressDialog(this.requireContext())
//        progressDialog.setMessage("正在加载...")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//        retrofitAPI.getGoalHistory(user_id).enqueue(object : Callback<GoalHistory> {
//            override fun onResponse(call: Call<GoalHistory>, response: Response<GoalHistory>) {
//                progressDialog.dismiss()
//                when (response.code()) {
//                    200 -> showToast("成功")
//                    400 -> showToast("格式錯誤")
//                    404 -> showToast("查無此資料")
//                    else -> showToast("伺服器故障: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<GoalHistory>, t: Throwable) {
//                progressDialog.dismiss()
//                showToast("伺服器故障")
//            }
//        })
//        return emptyList()
//    }

//    private fun getUserWeightHistory(user_id: Int): List<WeightHistory> {
//        val progressDialog = ProgressDialog(this.requireContext())
//        progressDialog.setMessage("正在加载...")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//        retrofitAPI.getWeightHistory(user_id).enqueue(object : Callback<WeightHistory> {
//            override fun onResponse(call: Call<WeightHistory>, response: Response<WeightHistory>) {
//                progressDialog.dismiss()
//                when (response.code()) {
//                    200 -> showToast("成功")
//                    400 -> showToast("格式錯誤")
//                    404 -> showToast("查無此資料")
//                    else -> showToast("伺服器故障: ${response.message()}")
//                }
//            }
//            override fun onFailure(call: Call<WeightHistory>, t: Throwable) {
//                progressDialog.dismiss()
//                showToast("伺服器故障")
//            }
//        })
//        return emptyList()
//    }
//
//    private fun getUserProfile(user_id: Int): Profile {
//        return profile
//    }
//
//    private fun checkConsecutiveMonths(goalHistory: List<GoalHistory>): Boolean {
//        // Implement logic to check consecutive months with weight loss in goal history
//        return false
//    }
//
//    private fun calculateLastMonthWeightLoss(weightHistory: List<WeightHistory>): Double {
//        // Implement logic to calculate last month's weight loss
//        return 0.0
//    }
//
//    private fun checkConsecutiveMonthsAchievement9(goalHistory: List<GoalHistory>): Boolean {
//        // Implement logic to check consecutive months for achievement 09
//        return false
//    }
//
//    private fun calculateTotalWeightLoss(weightHistory: List<WeightHistory>): Double {
//        // Implement logic to calculate total weight loss
//        return 0.0
//    }
//    private fun showToast(message: String) {
//        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
//    }
}
