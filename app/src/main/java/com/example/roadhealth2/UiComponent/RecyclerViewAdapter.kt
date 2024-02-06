package com.example.roadhealth2.UiComponent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.roadhealth2.DataPacket
import com.example.roadhealth2.R

class RecyclerViewAdapter : RecyclerView.Adapter<DataViewer>() {

    private var data= listOf<DataPacket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewer {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.data_viewer, parent, false)
        return DataViewer(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DataViewer, position: Int) {
        holder.heading.text=data[position].heading
        holder.description.text=data[position].description
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecyclerView(newData:List<DataPacket>)
    {
        data=newData
        notifyDataSetChanged()
    }
}

class DataViewer(itemView:View):RecyclerView.ViewHolder(itemView){
    val heading: TextView =itemView.findViewById(R.id.Heading)
    val description: TextView =itemView.findViewById(R.id.description)
}