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
import okhttp3.ResponseBody
import org.json.JSONArray
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AddAppointment : AppCompatActivity() {
    lateinit var appointmentName: EditText
    lateinit var selectDate: TextView
    lateinit var selectTime: TextView
    lateinit var participants: RecyclerView
    lateinit var createButton: Button
    lateinit var description : EditText
    private var participationList: ArrayList<String> = ArrayList()
    fun getGroupList(){
        val groupName = intent.getStringExtra("groupName")
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        apiInterface.getMembersByName(groupName!!).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@AddAppointment, "Got Participants", LENGTH_SHORT).show()
                Log.i("aaaresponse",response.body().toString())
                response.body()?.let {
                    var participantsJSON = JSONArray(it.string())
                    for(e in 0 until participantsJSON.length()){
                        participationList.add(participantsJSON[e].toString())
                    }
                    participants.adapter = ParticipantsAdapter(this@AddAppointment, participationList)
                    participants.layoutManager = LinearLayoutManager(this@AddAppointment, LinearLayoutManager.HORIZONTAL, false)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AddAppointment, "Failed to get participants", LENGTH_SHORT).show()
            }
        })

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_appointment)
        appointmentName = findViewById(R.id.appointment_name)
        selectDate = findViewById(R.id.select_date)
        selectTime = findViewById(R.id.select_time)
        participants = findViewById(R.id.participants)
        description = findViewById(R.id.appointment_body)
        getGroupList()

        val calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mHour = calendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = calendar.get(Calendar.MINUTE)
        var date = mYear.toString()+mMonth.toString()+mDay.toString()
        var time = mHour.toString()+":"+mMinute.toString()
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

        createButton = findViewById(R.id.create_button)
        createButton.setOnClickListener {
            val apiInterface = APIClient.getClient().create(APIInterface::class.java)
            val call:Call<ResponseBody> = apiInterface.createEvent(appointmentName.text.toString(),date.toString()+" "+time.toString(), participationList,description.toString())
            call.enqueue(object: Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(this@AddAppointment, "Succeeded to Create Event",
                        Toast.LENGTH_SHORT
                    ).show()
                    apiInterface.updateUsersEvent(appointmentName.text.toString(), participationList).enqueue(object: Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            Toast.makeText(this@AddAppointment, "Succeeded to Update Users' EventList",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@AddAppointment, MainActivity::class.java)
                            Log.i("aaaaaaa","startactivity")
                            startActivity(intent)
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(this@AddAppointment, "Failed to Update Users' EventList",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@AddAppointment, "Fail to Create Event", Toast.LENGTH_SHORT).show()
                }
            })
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