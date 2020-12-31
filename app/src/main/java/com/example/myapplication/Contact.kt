package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.json.JSONArray
import java.io.InputStream
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Contact : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var restaurantlist: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_contact, container, false)
        restaurantlist = view.findViewById<ListView>(R.id.restaurant_list)
        val jsonString: String? = read_json()
        jsonResult(jsonString)
        return view
    }

    fun read_json(): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = this.requireContext().assets.open("contacts.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun jsonResult(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)

        val list = ArrayList<ContactData>()
        var i = 0
        while(i < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            list.add(
                ContactData(
                    jsonObject.getString("name"),
                    jsonObject.getString("phone_number"),
                    jsonObject.getString("location")
                )
            )
            i++
        }
        Log.d("MyApplication", list.toString())
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