package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Contact : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var restaurantlist: ListView
    private lateinit var fab: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_contact, container, false)
        restaurantlist = view.findViewById(R.id.restaurant_list)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddPopup()
        }

        jsonResult(read_json())

        return view
    }

    fun showAddPopup() {
        val inflater = this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.contact_add_popup, null)
        var new_name = view.findViewById(R.id.ask_name) as EditText
        var new_phone_number = view.findViewById(R.id.ask_phone_number) as EditText
        var new_location = view.findViewById(R.id.ask_location) as EditText

        val AddPopup = AlertDialog.Builder(this.requireContext())
            .setTitle("Add New Contact")
            .setPositiveButton("ADD") { dialog, which ->
                var jsonString: String = read_json()
                val jsonArray = JSONArray(jsonString)
                var new_id: Int = 1
                if(jsonArray.length() != 0) new_id = jsonArray.getJSONObject(jsonArray.length() - 1).getInt("id") + 1

                val jsonObject = JSONObject()

                jsonObject.put("id", new_id)
                jsonObject.put("name", new_name.text)
                jsonObject.put("phone_number", new_phone_number.text)
                jsonObject.put("location", new_location.text)


                if (jsonString == "[]") jsonString = "[" + jsonObject.toString() + "]"
                else    jsonString = jsonString.slice(IntRange(0, jsonString.length - 2)) + "," + jsonObject.toString() + "]"

                this.requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use { output ->
                    output.write(jsonString.toByteArray())
                }
                jsonResult(jsonString)

                Toast.makeText(this.requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .create()

        AddPopup.setView(view)
        AddPopup.show()
    }

    fun read_json(): String {
        var json: String = "[]"
        try {
            val inputStream = this.requireContext().openFileInput("contacts.json")
            json = inputStream.bufferedReader().use { it.readText() }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if(json == "")  return "[]"
        else    return json
    }

    fun jsonResult(string: String) {
        val list = ArrayList<ContactData>()
        var i = 0

        val jsonArray = JSONArray(string)
        while(i < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            list.add(
                ContactData(
                    jsonObject.getInt("id"),
                    jsonObject.getString("name"),
                    jsonObject.getString("phone_number"),
                    jsonObject.getString("location")
                )
            )
            i++
        }

        val adapter = ContactAdapter(this.requireContext(), list)
        restaurantlist.adapter = adapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Contact.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Contact().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}