package com.example.bodyboost

import android.view.animation.Animation
import com.example.bodyboost.Model.Accuracy
import com.example.bodyboost.Model.Achievement
import com.example.bodyboost.Model.DailyBonus
import com.example.bodyboost.Model.Member
import com.example.bodyboost.Model.Profile
import com.example.bodyboost.Model.CustomFood
import com.example.bodyboost.Model.DietRecord
import com.example.bodyboost.Model.Food
import com.example.bodyboost.Model.GoalHistory
import com.example.bodyboost.Model.Setting
import com.example.bodyboost.Model.Store
import com.example.bodyboost.Model.UserAchievement
import com.example.bodyboost.Model.Users
import com.example.bodyboost.Model.WeightHistory
import com.example.bodyboost.sport.Sport
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.Serializable

interface RetrofitAPI {
    //------------------------accuracy---------------------------
    @POST("api/accuracy/add/")
    fun addaccuracy(
        @Body accuracyData: AccuracyData
    ): Call<Accuracy>
    data class AccuracyData(
        val label: String,
        val accuracy: Number,
        val sport_record_item_id:Int
    )
    @DELETE("api/accuracy/delete/{id}")
    fun deleteaccuracy(
        @Path("id") id:Int
    ): Call<Accuracy>

    @GET("api/accuracy/{id}")
    fun getAccuracyData(
        @Path("id") id:Int
    ): Call<Accuracy>
    //------------------------achievement------------------------------
    @GET("api/achievement/")
    fun getAchievementData(
        @Path("id") id:Int
    ): Call<UserAchievement>

    //------------------------animation------------------------------
    @GET("api/animation/")
    fun getAnimationData(
        @Path("id") id:Int
    ): Call<Animation>

    @POST("api/animation/add/")
    fun addAnimation(
        @Body animationData: AnimationData
    ):Call<Animation>
    data class AnimationData(
        val name: String,
        val animation: String,
        val image: String,
        val sport_id: Int
    )
    @DELETE("api/animation/delete/{id}")
    fun deleteAnimation(
        @Path("id") id:String
    ):Call<Animation>

    @PUT("api/animation/update/{id}")
    fun updateAnimation(
        @Path("id") id:String,
        @Body name: Animation
    ):Call<Animation>

    //------------------------authentication------------------------------
    @POST("api/authentication/authenticationForgetPasswordCode/")
    fun testUserPwd(
        @Body code:String,
        @Body userID:Int
    ): Call<Void>

    @POST("api/authentication/authenticationRegisterCode/")
    fun testRegisterNum(
        @Body registerNumData: RegisterNumData
    ): Call<Void>
    data class RegisterNumData(
        val code: String,
        val userID: Int
    )
    @POST("api/authentication/resendRegisterMail/")
    fun resendVerificationMail(@Body account: Users): Call<Users>

    @POST("api/authentication/sendForgetPasswordMail/")
    fun forgetPwdMail(@Body account:Users): Call<String>

    //------------------------user------------------------------
    @GET("api/users/{id}/")
    fun getSpecialUserInfo(
        @Path("id") id:Int
    ): Call<Users>
    @POST("api/users/add/")
    fun addUser(@Body addUserData: AddUserData): Call<Users>
    data class AddUserData(
        val account: String,
        val password: String,
        val email:String
    )
    @DELETE("api/users/delete/{id}/")
    fun deleteUser(
        @Path("id") id:Int
    ):Call<Users>

    @POST("api/users/login/google/")
    fun loginGoogle(
        @Query("email") email:String
    ):Call<Users>

    @POST("api/users/login/normal/")
    fun loginNormal(@Body loginData: LoginData): Call<Users>
    data class LoginData(
        val account: String,
        val password: String
    )
    @PUT("api/users/update/email/{id}/")
    fun updateEmail(
        @Path("id") id:Int,
        @Body email: Users
    ):Call<Void>
    @PUT("api/users/update/password/{id}/")
    fun updatePwd(
        @Path("id") id:Int,
        @Body password: Users
    ):Call<Void>
    @GET("api/users/{id}/")
    fun getUsers(
        @Path("id") id: Int
    ):Call<Users>
    //------------------------profile------------------------------
    @GET("api/profile/{id}")
    fun getUserProfile(
        @Path("id") id: Int
    ): Call<Profile>

    @POST("api/profile/add/")
    fun addProfile(@Body profileData: ProfileData): Call<Profile>
    data class ProfileData(
        val name: String,
        val gender: Int,
        val birthday: String,
        val height: Number,
        val weight: Number,
        val userID: Int
    )

    @DELETE("api/profile/delete/{id}")
    fun deleteProfile(
        @Path("id") id:String
    ):Call<Users>
    @PUT("api/profile/update/goal/{id}")
    fun update_goal(
        @Path("id") id: Int,
        @Body goal: Profile
    ): Call<Profile>
    @PUT("api/profile/update/weight/{id}")
    fun update_weight(
        @Path("id") id:Int,
        @Body updateWeight: Profile
    ):Call<Profile>
    data class UpdateWeightData(
        val weight: String,
        val weight_goal: Profile
    )
    @PUT("api/profile/update/bodyfat/{id}")
    fun update_fat(
        @Path("id") id: Int,
        @Body body_fat: Profile
    ): Call<Profile>


    @GET("api/profile/{id}/")
    fun getProfile(
        @Path("id") id: Int
    ):Call<Profile>
    @GET("api/profile/weightachievement/check/{id}")
    fun getweightachievement(
        @Path("id") id: Int
    ):Call<Profile>
    //------------------------DailyBonus------------------------------
    @POST("api/dailybonus/add/{id}")
    fun addDailyBonus(@Path("id") userId: Int, @Body dailyBonusData: DailyBonusData): Call<DailyBonus>
    data class DailyBonusData(
        val id: Int,
        val date: String,
        val user_id: Int
    )

    @GET("api/dailybonus/{id}")
    fun searchdailybonus(
        @Path("id") id: Int
    ): Call<DailyBonus>

    //------------------------member---------------------------
    @PUT("api/setting/update/{id}")
    fun Upgrade(
        @Path("id") id: Int,
        @Body memberData: MemberData
    ): Call<Member>
    data class MemberData(
        val member_type: String,
        val phone: String,
        val is_trial: Boolean,
        val payment_type: String
    )

    //------------------------Setting------------------------------
    @PUT("api/setting/update/{id}")
    fun UpdateSetting(
        @Path("id") id: Int,
        @Body settingData: SettingData
    ): Call<Void>
    data class SettingData(
        val theme: String,
        val anim_char_name: String,
        val is_alerted: Boolean,
        val alert_day: String,
        val alert_time: String
    )
    //------------------------NotificationHistory-----------------------------
    @POST("api/notificationhistory/add")
    fun addNotificationHistory(
        @Body notificationHistory: NotificationHistory): Call<DailyBonus>
    data class NotificationHistory(
        val content: String,
        val is_read: Boolean,
        val label: String,
        val create_at: String,
        val user_id: Int

    )
    //---------------------------WeightHistory-----------------------
    @GET("api/weighthistory/{id}")
    fun getWeightHistory(
        @Path("id") id: Int
    ): Call<WeightHistory>
    //---------------------------GoalHistory-----------------------
    @GET("api/goalhistory/{id}")
    fun getGoalHistory(
        @Path("id") id: Int
    ): Call<GoalHistory>

    // ------food---------------------------------------------------------------------------------------
    @GET("api/food/")
    fun getAllFood(
        @Query("page") page:Int,
        @Query("page_size") page_size:Int
    ): Call<List<Food>>
    // ------search food--------------------------------------------------------------------------------
    @GET("api/searchfood/foodtype/{id}/{userId}")
    fun searchFoodById(
        @Path("id") id:String,
        @Path("userId") userId:String,
        @Query("page") page:Int,
        @Query("page_size") page_size: Int
    ): Call<List<Food>>

    @GET("api/searchfood/store/{id}/{userId}")
    fun searchFoodByStore(
        @Query("page") page:Int,
        @Query("page_size") page_size: Int,
        @Path("id") id:String,
        @Path("userId") userId:String
    ): Call<Food>

    @GET("api/searchfood/word/{userId}")
    fun searchFoodByWord(
        @Query("name") name:String,
        @Query("page") page:Int,
        @Query("page_size") page_size: Int,
        @Path("userId") userId:String
    ): Call<Food>
    @DELETE("api/store/delete/{id}")
    fun deleteStore( @Path("id") id:String ): Call<Store>

    @PUT("api/store/update/{id}")
    fun updateStore(
        @Path("id") id:String,
        @Body name: Store
    ): Call<Void>
    // ------diet record--------------------------------------------------------------------------------
    @POST("api/dietrecord/add/")
    fun addDietRecord( @Body addDietRecordData: DietRecordData ): Call<DietRecord>
    data class DietRecordData(
        val date: String,
        val label: String,
        val serving_amount: Number,
        val name: String,
        val calorie: Number,
        val size: Number,
        val unit: String,
        val protein: Number,
        val fat: Number,
        val carb: Number,
        val sodium: Number,
        val modify: Boolean,
        val food_type_id: Int,
        val store_id: Int,
        val user_id: Int
    ) : Serializable

    @POST("api/dietrecord/add/many")
    fun addManyDietRecord( @Body addDietREcordDataMany: DietRecordDataMany): Call<DietRecord>
    data class DietRecordDataMany(
        val date: String,
        val label: String,
        val user_id: Int,
        val items: MutableList<DietRecordData2>
    ) : Serializable

    data class DietRecordData2(
        val serving_amount: Number,
        val name: String,
        val calorie: Number,
        val size: Number,
        val unit: String,
        val protein: Number,
        val fat: Number,
        val carb: Number,
        val sodium: Number,
        val modify: Boolean,
        val food_type_id: Int,
        val store_id: Int
    ) : Serializable

    @DELETE("api/dietrecord/delete/{id}")
    fun deleteDietRecord( @Path("id") id:String ): Call<DietRecord>

    @PUT("api/dietrecord/update/{id}")
    fun updateDietRecord(
        @Path("id") id:String,
        @Body updateDietRecordData: DietRecordData
    ):Call<Void>

    @GET("api/dietrecord/{id}")
    fun getDietRecord(
        @Path("id") id:String
    ): Call<List<DietRecord>>

    // ------custom food--------------------------------------------------------------------------------
    @POST("api/customfood/add")
    fun addCustomFood( @Body addCustomFoodData: CustomFoodData ): Call<List<CustomFood>>
    data class CustomFoodData(
        val name: String,
        val calorie:Number,
        val size: Number,
        val unit: String,
        val protein: Number?,
        val fat: Number?,
        val carb: Number?,
        val sodium: Number?,
        val modify: Boolean,
        val food_type_id: Int,
        val store_id: Int,
        val user_id: Int
    ) : Serializable

    @DELETE("api/customfood/delete/{id}")
    fun deleteCustomFood( @Path("id") id:String ): Call<List<CustomFood>>

    @PUT("api/customfood/update/{id}")
    fun updateCustomFood(
        @Path("id") id:String,
        @Body updateCustomFood: CustomFoodData
    ):Call<Void>

    @GET("api/customfood/{id}")
    fun getCustomFoodById(
        @Path("id") id: String,
        @Query("page") page:Int,
        @Query("page_size") page_size:Int
    ): Call<List<CustomFood>>

// -------------------------------------------------------------------------------------------------
// sport
    @GET("api/sport/{id}")
    fun getSportByUserId(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): Call<List<Sport>>
    // ------store--------------------------------------------------------------------------------------
    @GET("api/store/")
    fun getAllStore(): Call<List<Store>>

    @POST("api/store/add/")
    fun addStore( @Body name: StoreData ): Call<Store>
    data class StoreData(
        val name:String
    )

}

