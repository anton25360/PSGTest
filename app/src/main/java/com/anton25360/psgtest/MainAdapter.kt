package com.anton25360.psgtest


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_row.view.*


class MainAdapter(val dataArray: ArrayList<ArrayList<String>>) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val rowData = dataArray[position]
        holder.view.main_row_title.text = rowData[0]
        Picasso.get().load(rowData[1]).into(holder.view.main_row_thumbnail)
        holder.view.main_row_videoId.text = rowData[2]

    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view)