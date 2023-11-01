package com.example.bodyboost.Food

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.bodyboost.Model.Food
import com.example.bodyboost.R

class CustomFoodAdapter(private val context: Context, private val customFoodList: List<Food>) :
    BaseAdapter() {
    override fun getCount(): Int = customFoodList.size

    override fun getItem(position: Int): Any = customFoodList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_custom_food, parent, false)
        val listName = view.findViewById<TextView>(R.id.listName)
        val listCalorie = view.findViewById<TextView>(R.id.listCalories)
        val listRemove = view.findViewById<ImageButton>(R.id.listRemove)
        listName.text = customFoodList[position].name
        listCalorie.text = customFoodList[position].calorie.toString()
        listRemove.setOnClickListener {
            Toast.makeText(context, "移除自訂食物", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}