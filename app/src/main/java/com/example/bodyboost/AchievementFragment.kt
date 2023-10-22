package com.example.bodyboost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bodyboost.Model.DailyBonus
import com.example.bodyboost.Model.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AchievementFragment : Fragment(){
    private lateinit var dailyBonus: DailyBonus
    private val retrofitAPI = RetrofitManager.getInstance()
    private lateinit var goal_08_txt: TextView
    private lateinit var goal_08_img:ImageView
    private lateinit var goal_09_txt: TextView
    private lateinit var goal_09_img:ImageView
    private lateinit var profile: Profile
    private val currentUser = UserSingleton.user
    private var count : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achievement, container, false)
            goal_08_img = view.findViewById(R.id.goal_08_img)
            goal_08_txt = view.findViewById(R.id.goal_08_txt)
            goal_09_img = view.findViewById(R.id.goal_09_img)
            goal_09_txt = view.findViewById(R.id.goal_09_txt)
            currentUser?.let { getDailyBouns(it.id) }
            if(count>30){
                if(profile.weight.toInt() - profile.weight_goal.toInt()>2){
                    achieve08()
                }
            }else if(count>92){
                if(profile.weight.toInt() - profile.weight_goal.toInt()>10){
                    achieve09()
                }
            }else{

            }


        return view
    }
    private fun achieve08(){
        if(profile.weight_goal!=null){
            currentUser?.let { retrofitAPI.getweightachievement(it.id) }
            goal_08_txt.text = "瘦身達人"
            goal_08_img.setImageResource(R.drawable.goal_08)
        }
    }

    private fun achieve09(){

            currentUser?.let { retrofitAPI.getweightachievement(it.id) }
            goal_09_txt.text = "身材改造師"
            goal_09_img.setImageResource(R.drawable.goal_09)

    }
    private fun getDailyBouns(id: Int) {
        currentUser?.let {
            retrofitAPI.searchdailybonus(it.id).enqueue(object :Callback<DailyBonus>{
                override fun onResponse(call: Call<DailyBonus>, response: Response<DailyBonus>) {
                    when (response.code()) {
                        200 -> {
                            val currentUsers = response.body()
                            if (currentUsers != null) {
                                dailyBonus.date.forEach { count++ }
                            }
                        }
                        400 -> showToast("格式錯誤")
                        404 -> showToast("查無此資料")
                        else -> showToast("伺服器故障: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DailyBonus>, t: Throwable) {
                    showToast("伺服器故障")
                }

            })
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
}