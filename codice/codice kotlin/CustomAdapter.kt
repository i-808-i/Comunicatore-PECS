package com.example.provamaturita

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val courseList: List<PECS>, private val backgroundColors: Array<String>): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun OnItemClick(position: Int, item: PECS)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPECS = courseList[position]
        val backgroundColor = backgroundColors[position % backgroundColors.size]
        val image = currentPECS.image
        val head = currentPECS.catImg

        holder.imageView.setImageResource(image)
        holder.headTextView.text = head
        holder.cardView.setBackgroundColor(Color.parseColor(backgroundColor))

        setAnimation(holder.itemView)

        holder.itemView.setOnClickListener {
            itemClickListener?.OnItemClick(position, currentPECS)
        }
    }

    private fun setAnimation(view : View){
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = 900
        view.startAnimation(anim)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val headTextView = itemView.findViewById<TextView>(R.id.head)
        val cardView = itemView.findViewById<CardView>(R.id.cibo)
    }
}