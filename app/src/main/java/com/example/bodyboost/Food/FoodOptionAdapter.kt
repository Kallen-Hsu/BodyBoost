package com.example.bodyboost.Food

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bodyboost.R
import com.example.bodyboost.RetrofitAPI

class FoodOptionAdapter(private val context: Context, private val foodList: List<RetrofitAPI.DietRecordData>) :
    BaseAdapter() {
    override fun getCount(): Int = foodList.size

    override fun getItem(position: Int): Any = foodList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_option_item, parent, false)
        val listName = view.findViewById<TextView>(R.id.option_name)
        val listQuantity = view.findViewById<TextView>(R.id.option_size)
        val listUnit = view.findViewById<TextView>(R.id.option_unit)
        listName.text = foodList[position].name
        listQuantity.text = foodList[position].size.toString()
        listUnit.text = foodList[position].unit
        return view
    }
}