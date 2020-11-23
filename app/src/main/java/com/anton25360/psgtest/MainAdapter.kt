package com.anton25360.psgtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_row.view.*

class MainAdapter(val dataArray: ArrayList<ArrayList<String>>, val listener: MainActivity) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row, parent, false)
        return CustomViewHolder(cellForRow, listener)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val rowData = dataArray[position]
        holder.view.main_row_title.text = rowData[0]
        Picasso.get().load(rowData[1]).into(holder.view.main_row_thumbnail)
    }
}

class CustomViewHolder(val view: View, val listener: MainActivity): RecyclerView.ViewHolder(view),
        View.OnClickListener{
    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //gets position of item
        val position: Int = adapterPosition
        if (position != RecyclerView.NO_POSITION) { //ensure the position is valid
            listener.onItemClick(position)
        }
    }
}

interface  OnItemClickListener {
    fun onItemClick(position: Int)
}