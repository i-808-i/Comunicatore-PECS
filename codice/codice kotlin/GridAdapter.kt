package com.example.provamaturita

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class GridAdapter(private val imageList: List<Int>, private val textList: List<String>, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private val clickedImages = mutableListOf<Int>()


    interface OnItemClickListener{
        fun onItemClick(imageId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cucstom_laygrid, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageResource = imageList[position]
        val text = textList[position]
        holder.imageView.setImageResource(imageResource)
        holder.textView.text = text

        holder.itemView.setOnClickListener{
            itemClickListener.onItemClick(imageResource)
            clickedImages.add(imageResource)
        }
    }


    fun getClickedImages(): List<Int> {
        return clickedImages.toList()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.nome)
    }
}

