package com.madcamp.eattogether

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ReviewFragment : Fragment() {
    private lateinit var appointmentList: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private var appointmentListData: ArrayList<Appointment> = arrayListOf(
        Appointment("", "", arrayListOf("a", "b", "c"), arrayListOf("d", "e"), true)
        , Appointment("", "", arrayListOf("a", "b", "c"), arrayListOf("d", "e"), true)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)
        appointmentList = view.findViewById(R.id.appointment_list)
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