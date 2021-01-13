package com.madcamp.eattogether

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.Profile
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupFragment : Fragment() {
    private lateinit var appointmentList: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private var appointmentListData: ArrayList<Appointment> = ArrayList()


    val userId = Profile.getCurrentProfile().id
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val apiInterface = APIClient.getClient().create(APIInterface::class.java)
        val view = inflater.inflate(R.layout.fragment_group, container, false)
        appointmentList = view.findViewById(R.id.appointment_list)
        apiInterface.getEventList(userId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(context, "Succeeded to get eventlist", Toast.LENGTH_SHORT).show()
                response.body()?.let {
                    val eventListFromJSON = JSONArray(it.string())
                    for (e in 0 until eventListFromJSON.length()) {
                        apiInterface.getOneEvent("sfgsfg"/*eventListFromJSON[e].toString()*/)
                            .enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    response.body()?.let { e ->
                                        /*
                                        Log.d("@@@@@@@@@@@@@","@@@@@@@@@@@@@@@ " + e.string() + "!!!!!!!!!!!!!")
                                        var responseBody : String = e.string()
                                        if(responseBody == "") {
                                            Toast.makeText(this@GroupFragment.context, "Empty response body", LENGTH_SHORT).show()
                                            Log.i("aaaaaastring","empty")
                                        }
                                        else {
                                                                                Log.i("aaaaaastring",responseBody)
                                         */

                                        var JSON1 = JSONObject(e.string())
                                        var members = JSON1.getString("members")
                                        Log.d("#@@@@@@@@@@@@@@", members)
                                        var members1 = JSONArray(members)

                                        var members2: ArrayList<String> = ArrayList()
                                        for (i in 0 until members1.length()) {
                                            members2.add(members1[i].toString())
                                        }
                                        var appointment: Appointment = Appointment(
                                            JSON1.getString("name"),
                                            JSON1.getString("date"),
                                            members2
                                        )
                                        Log.d("#@@@@@@@@@@@@@@", appointment.name)
                                        Log.d("#@@@@@@@@@@@@@@", appointment.time)
                                        appointmentListData.add(appointment)
                                        //}
                                    }
                                    Log.d("appointmentlistdata", appointmentListData.size.toString())
                                    appointmentAdapter = AppointmentAdapter(context!!, appointmentListData)
                                    appointmentList.adapter = appointmentAdapter
                                    appointmentList.layoutManager = LinearLayoutManager(context)

                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    Log.i("aaaaaa", "failed")
                                }
                            })
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "Failed to get group list", Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAppointmentList()
    }

    private fun loadAppointmentList() {
        appointmentAdapter = AppointmentAdapter(this.requireContext(), appointmentListData)
        appointmentList.adapter = appointmentAdapter
        appointmentList.layoutManager = LinearLayoutManager(context)
    }
}