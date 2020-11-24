package com.anton25360.psgtest


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_row.view.*

class CommentsAdapter(val dataArray: ArrayList<ArrayList<String>>) : RecyclerView.Adapter<CustomViewHolder2>() {

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder2 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.comment_row, parent, false)
        return CustomViewHolder2(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder2, position: Int) {
        val rowData = dataArray[position]
        holder.view.comment_text.text = rowData[0]
        holder.view.comment_author.text = rowData[1]
    }
}

class CustomViewHolder2(val view:View): RecyclerView.ViewHolder(view) {

}