package com.example.bodyboost.food

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bodyboost.Model.Food
import com.example.bodyboost.R

class FoodListAdapter(private val context: Context, private val foodList: List<Food>) : BaseAdapter() {
    override fun getCount(): Int = foodList.size

    override fun getItem(position: Int): Any = foodList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        val listName = view.findViewById<TextView>(R.id.listName)
        val listCalorie = view.findViewById<TextView>(R.id.listCalories)
        listName.text = foodList[position].name
        listCalorie.text = foodList[position].calorie.toString()
        return view
    }
}