package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat

class ContactAdapter(val context: Context, val list:ArrayList<ContactData>):BaseAdapter() {

    private var filtered: ArrayList<ContactData>? = list

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = LayoutInflater.from(context).inflate(R.layout.contact_layout, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.pimage)
        val name = view.findViewById<TextView>(R.id.pname)
        val food_type = view.findViewById<TextView>(R.id.pfood_type)
        val phone_number = view.findViewById<TextView>(R.id.pphone_number)

        name.text = list[position].name.toString()
        food_type.text = list[position].food_type.toString()
        phone_number.text = list[position].phone_number.toString()
        if(list[position].restaurant_type == 1){
            if((name.text.length + food_type.text.length) % 2 == 0){
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.delivery1))
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.delivery2))
            }
        }
        else{
            if((name.text.length + food_type.text.length) % 2 == 0){
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.visit1))
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.visit2))
            }
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }
}