package com.example.bodyboost.sport

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bodyboost.R

class SportHorizontalListAdapter(private var sportHorizontalItemList: List<SportHorizontalItem>) :
    RecyclerView.Adapter<SportHorizontalListAdapter.SportHorizontalListViewHolder>() {

    private var onItemClickListener: SportHorizontalListAdapter.OnItemClickListener? = null

    inner class SportHorizontalListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.sport_horizontal_list_text)
        val image: ImageView = itemView.findViewById(R.id.sport_horizontal_list_img)
        val btn: LinearLayout = itemView.findViewById(R.id.sport_horizontal_list_Btn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SportHorizontalListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sport_horizontal_item, parent, false)
        return SportHorizontalListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sportHorizontalItemList.size
    }

    override fun onBindViewHolder(holder: SportHorizontalListViewHolder, position: Int) {
        Glide.with(holder.itemView).load(sportHorizontalItemList[position].image).into(holder.image)
        holder.title.text = sportHorizontalItemList[position].name

        holder.btn.setOnClickListener {
            onItemClickListener?.onItemClick(sportHorizontalItemList[position])
        }

    }

    interface OnItemClickListener {
        fun onItemClick(sportHorizontalItem: SportHorizontalItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}