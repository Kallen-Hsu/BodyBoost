package com.example.bodyboost.Food

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bodyboost.Model.DietRecord
import com.example.bodyboost.R
import com.example.bodyboost.RecordFragment

class FoodRecordAdapter(private val context: RecordFragment, private val recordList: List<DietRecord>) :
    BaseAdapter() {
    override fun getCount(): Int = recordList.size

    override fun getItem(position: Int): Any = recordList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context.context)
        val view = inflater.inflate(R.layout.list_record_item, parent, false)
        val listName = view.findViewById<TextView>(R.id.record_name)
        val listCalorie = view.findViewById<TextView>(R.id.record_calorie)
        listName.text = recordList[position].name
        listCalorie.text = recordList[position].calorie.toString()
        return view
    }
}