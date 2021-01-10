package com.madcamp.eattogether

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(private val context: Context, private var list: List<Appointment>):
RecyclerView.Adapter<AppointmentAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.appointment_list_item,parent,false),
            context
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setView(list[position])
    }

    class ViewHolder(itemView: View, private val context:Context): RecyclerView.ViewHolder(itemView) {
        fun setView(appointment:Appointment) {
            val location: TextView = itemView.findViewById(R.id.location)
            val time: TextView = itemView.findViewById(R.id.time)
            val attendentList: ListView = itemView.findViewById(R.id.attendent_list)
            val waitResponseList: ListView = itemView.findViewById(R.id.wait_response_list)

            val attendentListAdapter:ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, appointment.attendants)
            val waitResponseListAdapter:ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, appointment.waitResponseList)

            attendentList.adapter = attendentListAdapter
            waitResponseList.adapter = waitResponseListAdapter
        }
    }
}