package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class ContactAdapter(val context: Context, val list:ArrayList<ContactData>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.contact_layout, parent, false)
        val name = view.findViewById<TextView>(R.id.pname)
        val phone_number = view.findViewById<TextView>(R.id.pphone_number)
        val location = view.findViewById<TextView>(R.id.plocation)

        name.text = list[position].name.toString()
        phone_number.text = list[position].phone_number.toString()
        location.text = list[position].location.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}