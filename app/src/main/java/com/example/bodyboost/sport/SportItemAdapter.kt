package com.example.bodyboost.sport

import android.content.Intent
import android.media.MediaParser
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bodyboost.MainActivity
import com.example.bodyboost.R

class SportItemAdapter(private var sportItemList: List<Sport>) :
    RecyclerView.Adapter<SportItemAdapter.SportItemViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    inner class SportItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val sportAnimation: ImageView = itemView.findViewById(R.id.sport_animation)
        val sportName: TextView = itemView.findViewById(R.id.sport_name)
        val sportDefaultTime: TextView = itemView.findViewById(R.id.sport_default_time)
        val sportType: TextView = itemView.findViewById(R.id.sport_type_text)
        val sportCounting: TextView = itemView.findViewById(R.id.sport_counting_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sport_item, parent, false)
        return SportItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sportItemList.size
    }

    override fun onBindViewHolder(holder: SportItemViewHolder, position: Int) {
        Glide.with(holder.itemView).load(sportItemList[position].animation.animation).into(holder.sportAnimation)
        holder.sportName.text = sportItemList[position].name

        val timePair = secondsToMinutesAndSeconds(sportItemList[position].default_time)
        holder.sportDefaultTime.text = String.format("%02d:%02d", timePair.first, timePair.second)

        if (sportItemList[position].type == "aerobics") {
            holder.sportType.text = "有氧"
        } else if (sportItemList[position].type == "anaerobic") {
            holder.sportType.text = "無氧"
        }

        if (!sportItemList[position].is_count) {
            holder.sportCounting.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
//            Toast.makeText(holder.itemView.context, sportItemList[position].id.toString() + " success message", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, SportInfoActivity::class.java)
            intent.putExtra("id", sportItemList[position].id)
            intent.putExtra("name", sportItemList[position].name)
            intent.putExtra("description", sportItemList[position].description)
            intent.putExtra("default_time", sportItemList[position].default_time)
            intent.putExtra("interval", sportItemList[position].interval)
            intent.putExtra("is_count", sportItemList[position].is_count)
            intent.putExtra("met", sportItemList[position].met)
            intent.putExtra("animation", sportItemList[position].animation.animation)
            holder.itemView.context.startActivity(intent)
//            onItemClickListener?.onItemClick(sportItemList[position])
        }
    }

    private fun secondsToMinutesAndSeconds(totalSeconds: Float): Pair<Int, Int> {
        val minutes = totalSeconds.toInt() / 60
        val seconds = totalSeconds.toInt() % 60
        return Pair(minutes, seconds)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(sport: Sport)
    }


}