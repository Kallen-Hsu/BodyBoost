package com.example.bodyboost.sport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitManager
import java.util.ArrayList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SportFragment : Fragment() {
    private val retrofitAPI = RetrofitManager.getInstance()

    private lateinit var bottomView1: View

    private lateinit var sportFrameLayout: FrameLayout
    private lateinit var searchBarEditText: EditText
    private lateinit var sportCancelButton: Button

    private lateinit var sportItemListView: RecyclerView
    private lateinit var lastSportListView: RecyclerView
    private lateinit var comboSportListView: RecyclerView

    private lateinit var sportItemAdapter: SportItemAdapter
    private var sportItemList: List<Sport> = ArrayList<Sport>()

    private var lastSportList = ArrayList<SportHorizontalItem>()
    private lateinit var lastSportListAdapter: SportHorizontalListAdapter

    private var comboSportList = ArrayList<SportHorizontalItem>()
    private lateinit var comboSportListAdapter: SportHorizontalListAdapter

    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sports, container, false)

        sportFrameLayout = view.findViewById(R.id.sportFrameLayout)
        searchBarEditText = view.findViewById(R.id.searchBarEditText)
        sportCancelButton = view.findViewById(R.id.sportCancelButton)
        bottomView1 = inflater.inflate(R.layout.sport_bottom_view_1, null)

        val sportInflater:LayoutInflater  = layoutInflater
        val bottomView1: View = sportInflater.inflate(R.layout.sport_bottom_view_1, null)
        sportFrameLayout.addView(bottomView1)

        addSportItemData()
        addLastSportItemData()
        addComboSportItemData()
        updateSportList(bottomView1, sportItemList)
        updateLastSportList(bottomView1, lastSportList)
        updateComboSportList(bottomView1, comboSportList)

//        sportItemListView = bottomView1.findViewById(R.id.sportRecyclerView)
//
//        sportItemListView.layoutManager = LinearLayoutManager(view.context)
//        addSportItemData()
//        sportItemAdapter = SportItemAdapter(sportItemList)
//        sportItemListView.adapter = sportItemAdapter

        searchBarEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // 当 EditText 获得焦点时执行的操作
                changeFrameLayout()
            } else {
                // 当 EditText 失去焦点时执行的操作
            }
        }

//        searchBarEditText.addTextChangedListener (object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // 在文本变化时执行的操作
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                // 在文本改变之后执行的操作
//            }
//        })
        sportCancelButton.setOnClickListener{ goneSportCancelButton() }

        return view
    }

    private fun updateLastSportList(bottomView1: View, sportList: ArrayList<SportHorizontalItem>) {
        lastSportListView = bottomView1.findViewById(R.id.last_sport_list_view)
        lastSportListView.setHasFixedSize(true)
        lastSportListView.layoutManager = LinearLayoutManager(bottomView1.context, LinearLayoutManager.HORIZONTAL, false)
        lastSportListAdapter = SportHorizontalListAdapter(sportList)
        lastSportListView.adapter = lastSportListAdapter

        lastSportListAdapter.setOnItemClickListener(object: SportHorizontalListAdapter.OnItemClickListener {
            override fun onItemClick(sportHorizontalItem: SportHorizontalItem) {
                // 當點擊 List 時，跳轉頁面
                Toast.makeText(view.context, sportHorizontalItem.id.toString() + " success message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateComboSportList(bottomView1: View, sportList: ArrayList<SportHorizontalItem>) {
        comboSportListView = bottomView1.findViewById(R.id.combo_sport_list_view)
        comboSportListView.setHasFixedSize(true)
        comboSportListView.layoutManager = LinearLayoutManager(bottomView1.context, LinearLayoutManager.HORIZONTAL, false)
        comboSportListAdapter = SportHorizontalListAdapter(sportList)
        comboSportListView.adapter = comboSportListAdapter

        comboSportListAdapter.setOnItemClickListener(object: SportHorizontalListAdapter.OnItemClickListener {
            override fun onItemClick(sportHorizontalItem: SportHorizontalItem) {
                // 當點擊 List 時，跳轉頁面
                Toast.makeText(view.context, sportHorizontalItem.id.toString() + "combo success message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSportList(bottomView1: View, sportList: List<Sport>) {
        sportItemListView = bottomView1.findViewById(R.id.sportRecyclerView)
        sportItemListView.setHasFixedSize(true)
        sportItemListView.layoutManager = LinearLayoutManager(bottomView1.context)
        sportItemAdapter = SportItemAdapter(sportList)
        sportItemListView.adapter = sportItemAdapter

        sportItemAdapter.setOnItemClickListener(object : SportItemAdapter.OnItemClickListener {
            override fun onItemClick(sport: Sport) {
                // 當點擊 List 時，跳轉頁面
                Toast.makeText(view.context, sport.id.toString() + " success message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addLastSportItemData() {
        lastSportList.add(
            SportHorizontalItem(
                id = 1,
                name = "開合跳",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-jumping-jack-light-image.gif"
            ))
        lastSportList.add(
            SportHorizontalItem(
                id = 2,
                name = "伏地挺身",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-push-up-light-image.gif"
            ))
        lastSportList.add(
            SportHorizontalItem(
                id = 3,
                name = "手肘碰膝蓋",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-elbow-knee-touch2-light-image.gif"
            ))
        lastSportList.add(
            SportHorizontalItem(
                id = 4,
                name = "伏地挺身",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-push-up-light-image.gif"
            ))

        lastSportList.add(
            SportHorizontalItem(
                id = 5,
                name = "伏地挺身",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-push-up-light-image.gif"
            ))
    }

    private fun addComboSportItemData() {
        comboSportList.add(
            SportHorizontalItem(
                id = 1,
                name = "減肥1",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-jumping-jack-light-image.gif"
            ))
        comboSportList.add(
            SportHorizontalItem(
                id = 2,
                name = "減肥2",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-push-up-light-image.gif"
            ))
        comboSportList.add(
            SportHorizontalItem(
                id = 3,
                name = "增肌",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-elbow-knee-touch2-light-image.gif"
            ))
        comboSportList.add(
            SportHorizontalItem(
                id = 4,
                name = "減脂1",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-jumping-jack-light-image.gif"
            ))

        comboSportList.add(
            SportHorizontalItem(
                id = 5,
                name = "瘦身&增肌&減脂運動組合",
                image = "https://storage.googleapis.com/bodyboost-bucket/animation_img/Lisa-elbow-knee-touch2-light-image.gif"
            ))
    }

    private fun addSportItemData() {
        val call: Call<List<Sport>> = retrofitAPI.getSportByUserId(2, 1, 20)
        call.enqueue(object: Callback<List<Sport>> {
            override fun onResponse(call: Call<List<Sport>>, response: Response<List<Sport>>) {
                if (response.code() == 200) {
                    Toast.makeText(view.context, "success message", Toast.LENGTH_SHORT).show()
                    sportItemList = response.body()!!
                    sportItemAdapter = SportItemAdapter(sportItemList)
                    sportItemListView.adapter = sportItemAdapter
                } else if (response.code() == 404) {
                    Toast.makeText(view.context, "not found message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Sport>>, t: Throwable) {
                Toast.makeText(view.context, "error message", Toast.LENGTH_SHORT).show()
            }
        })
    }

    public fun setSearchBarEditTextClearFocus() {
        searchBarEditText.clearFocus()
    }

    private fun changeFrameLayout() {
        val sportInflater:LayoutInflater  = layoutInflater
        val bottomView2: View = sportInflater.inflate(R.layout.sport_bottom_view_2, null)

        sportCancelButton.visibility = View.VISIBLE
        sportFrameLayout.removeAllViews()
        sportFrameLayout.addView(bottomView2)
    }

    private fun goneSportCancelButton() {
        sportCancelButton.visibility = View.GONE

        updateSportList(bottomView1, sportItemList)
        updateLastSportList(bottomView1, lastSportList)
        updateComboSportList(bottomView1, comboSportList)

        sportFrameLayout.removeAllViews()
        sportFrameLayout.addView(bottomView1)
    }

}