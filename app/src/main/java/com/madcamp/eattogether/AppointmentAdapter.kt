package com.madcamp.eattogether

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
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
            val name: TextView = itemView.findViewById(R.id.location)
            val time: TextView = itemView.findViewById(R.id.time)
            val description: TextView = itemView.findViewById(R.id.description)
            val attendentList: ListView = itemView.findViewById(R.id.attendent_list)
            description.isClickable = true
            description.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(description.text.toString()))
                startActivity(context, browserIntent, null)
            }
            name.text = appointment.name
            time.text = appointment.time
            val attendentListAdapter:ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, appointment.attendants)
            attendentList.adapter = attendentListAdapter
        }
    }
}