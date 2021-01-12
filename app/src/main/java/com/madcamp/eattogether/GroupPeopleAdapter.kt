package com.madcamp.eattogether

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.TextView


class GroupPeopleAdapter(private val context: Context, private var list: List<Phone>):RecyclerView.Adapter<GroupPeopleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var mPhone: Phone
        init{
            val addbtn = itemView.findViewById<CheckBox>(R.id.checkBox)
            addbtn.isChecked = false
            addbtn.setOnCheckedChangeListener { buttonView, isChecked ->
                mPhone.ifadded = true
            }
        }
        fun setView(phone: Phone) {
            this.mPhone = phone
            val name = itemView.findViewById<TextView>(R.id.name)
            val phoneNum = itemView.findViewById<TextView>(R.id.phoneNum)
            name.text = mPhone.name
            phoneNum.text = mPhone.phoneNumber
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: GroupPeopleAdapter.ViewHolder, position: Int) {
        holder.setView(list[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupPeopleAdapter.ViewHolder {
        return GroupPeopleAdapter.ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.group_person_item, parent, false)
        )
    }
}