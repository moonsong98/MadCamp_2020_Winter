package com.madcamp.eattogether

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.util.*

class AddAppointment : AppCompatActivity() {
    lateinit var appointmentName: EditText
    lateinit var selectDate: TextView
    lateinit var selectTime: TextView
    lateinit var participants: RecyclerView
    lateinit var createButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment)

        appointmentName = findViewById(R.id.appointment_name)
        selectDate = findViewById(R.id.select_date)
        selectTime = findViewById(R.id.select_time)
        participants = findViewById(R.id.participants)

        val groupName = intent.getStringExtra("groupName")
        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)
        selectDate.isClickable = true
        selectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    selectDate.text = "$year 년 ${month + 1} 월 $dayOfMonth 일"
                },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.show()
        }

        selectTime.isClickable = true
        selectTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, i: Int, i1: Int ->
                    selectTime.text = "$i 시 $i1 분"
                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }
        participants.adapter = ParticipantsAdapter(this, listOf("James", "Anna"))
        participants.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        createButton = findViewById(R.id.create_button)
        createButton.setOnClickListener {
            this.finish()
        }

        /* Test
        val test:TextView = findViewById(R.id.test)
        var text = test.text
        test.isClickable = true
        if(!text.startsWith("http://") && !text.startsWith("https://"))
            text = "http://" + text
        test.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(text.toString()))
            startActivity(browserIntent)
        }*/

    }
    class ParticipantsAdapter(private val context: Context, private var list: List<String>) :
        RecyclerView.Adapter<ParticipantsAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.participants_list_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setName(list[position])
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nameTextView:TextView =  itemView.findViewById(R.id.name)
            fun setName(name:String){
                nameTextView.text = name
            }
        }
    }

}