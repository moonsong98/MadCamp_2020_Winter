package com.example.myapplication

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Contact : Fragment() {
    // TODO: Rename and change types of para
    //  meters
    private lateinit var restaurantlist: ListView
    private lateinit var fab: View
    val list = ArrayList<ContactData>()
    private lateinit var adapter: ContactAdapter

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

        showlist(read_json())

        return view
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

    fun showlist(string: String) {
        val list = ArrayList<ContactData>()
        var i = 0

        val jsonArray = JSONArray(string)
        while(i < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            val locationjsonObject = jsonObject.getJSONObject("location")
            list.add(
                    ContactData(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("food_type"),
                            jsonObject.getInt("restaurant_type"),
                            jsonObject.getString("phone_number"),
                            Place(
                                locationjsonObject.getString("address"),
                                locationjsonObject.getDouble("latitude"),
                                locationjsonObject.getDouble("longitude")
                            )
                    )
            )
            i++
        }

        adapter = ContactAdapter(this.requireContext(), list)

        restaurantlist.adapter = adapter
        restaurantlist.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as ContactData
            showRestaurantPopup(item)
        }
    }

    fun showAddPopup() {
        val inflater = this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.contact_add_popup, null)
        val addButton = view.findViewById(R.id.add_button) as ImageButton
        val cancelButton = view.findViewById(R.id.cancel_button) as ImageButton
        var new_name = view.findViewById(R.id.ask_name) as EditText
        var new_food_type = view.findViewById(R.id.ask_food_type) as EditText
        val radioGroup = view.findViewById(R.id.radio_group) as RadioGroup
        var new_restaurant_type: Int = 0
        val restaurant_type_warn = view.findViewById(R.id.restaurant_type_warn) as TextView
        var new_phone_number = view.findViewById(R.id.ask_phone_number) as EditText
        var new_location = view.findViewById(R.id.ask_location) as EditText

        radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener{ group, checkedId ->
                    val radio: RadioButton = view.findViewById(checkedId)
                    if(radio.text == "Delivery")    new_restaurant_type = 1
                    else    new_restaurant_type = 2
                }
        )

        val AddPopup = AlertDialog.Builder(this.requireContext())
            .setTitle("Add New Restaurant")
            .setCancelable(false)
            .create()

        cancelButton.setOnClickListener {
            AddPopup.dismiss()
        }

        addButton.setOnClickListener {
            val address = getfromlocationname(new_location.text.toString())
            if(new_name.text.toString().replace(" ", "").equals("")){
                restaurant_type_warn.visibility = View.GONE
                new_name.setError("Enter Name", )
            }
            else if(new_food_type.text.toString().replace(" ", "").equals("")){
                restaurant_type_warn.visibility = View.GONE
                new_food_type.setError("Enter Food Type")
            }
            else if(new_restaurant_type == 0){
                restaurant_type_warn.visibility = View.VISIBLE
            }
            else if(address == null && new_restaurant_type == 2){
                Toast.makeText(this.requireContext(), "No such location", Toast.LENGTH_SHORT).show()
            }
            else{
                restaurant_type_warn.visibility = View.GONE
                var jsonString: String = read_json()
                val jsonArray = JSONArray(jsonString)
                var new_id: Int = 1
                if(jsonArray.length() != 0) new_id = jsonArray.getJSONObject(jsonArray.length() - 1).getInt("id") + 1

                val jsonObject = JSONObject()
                val locationjsonObject = JSONObject()

                jsonObject.put("id", new_id)
                jsonObject.put("name", new_name.text)
                jsonObject.put("food_type", new_food_type.text)
                jsonObject.put("restaurant_type", new_restaurant_type)
                jsonObject.put("phone_number", new_phone_number.text)
                if(address == null){
                    locationjsonObject.put("address", "")
                    locationjsonObject.put("latitude", 0.0)
                    locationjsonObject.put("longitude", 0.0)
                }
                else{
                    locationjsonObject.put("address", address.getAddressLine(0))
                    locationjsonObject.put("latitude", address.latitude)
                    locationjsonObject.put("longitude", address.longitude)
                }
                jsonObject.put("location", locationjsonObject)

                if (jsonString == "[]") jsonString = "[" + jsonObject.toString() + "]"
                else    jsonString = jsonString.slice(IntRange(0, jsonString.length - 2)) + "," + jsonObject.toString() + "]"

                this.requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use { output ->
                    output.write(jsonString.toByteArray())
                }
                showlist(jsonString)

                AddPopup.dismiss()
                Toast.makeText(this.requireContext(), "ADDED", Toast.LENGTH_SHORT).show()
            }
        }

        AddPopup.setView(view)
        AddPopup.show()
    }

    private fun getfromlocationname(location_string: String): Address? {
        val geocoder: Geocoder = Geocoder(this.requireContext())
        try {
            val LocationResult: List<Address> = geocoder.getFromLocationName(location_string, 1);
            if(LocationResult.isEmpty())    return null
            return LocationResult.get(0)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun showRestaurantPopup(item: ContactData) {
        val inflater = this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.contact_restaurant_popup, null)
        val editButton = view.findViewById(R.id.edit_button) as ImageButton
        val deleteButton = view.findViewById(R.id.delete_button) as ImageButton
        val cancelButton = view.findViewById(R.id.cancel_button) as ImageButton
        val editokButton = view.findViewById(R.id.edit_ok_button) as ImageButton
        val editcancelButton = view.findViewById(R.id.edit_cancel_button) as ImageButton
        val askrestaurantdelete = view.findViewById(R.id.ask_restaurant_delete) as TextView
        val deleteokButton = view.findViewById(R.id.delete_ok_button) as ImageButton
        val deletecancelButton = view.findViewById(R.id.delete_cancel_button) as ImageButton
        var title = view.findViewById(R.id.title) as TextView
        var pimage = view.findViewById(R.id.pimage) as ImageView
        var food_type = view.findViewById(R.id.nfood_type) as TextView
        var phone_number = view.findViewById(R.id.nphone_number) as TextView
        var location = view.findViewById(R.id.nlocation) as TextView
        var edit_name = view.findViewById<TextView>(R.id.edit_name)
        var edit_food_type = view.findViewById<TextView>(R.id.edit_food_type)
        var edit_phone_number = view.findViewById<TextView>(R.id.edit_phone_number)
        var edit_location = view.findViewById<TextView>(R.id.edit_location)
        var address: Address?

        if(item.restaurant_type == 1){
            if((item.name.length + item.food_type.length) % 2 == 0){
                pimage.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.delivery1))
            }
            else{
                pimage.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.delivery2))
            }
        }
        else{
            if((item.name.length + item.food_type.length) % 2 == 0){
                pimage.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.visit1))
            }
            else{
                pimage.setImageDrawable(ContextCompat.getDrawable(this.requireContext(), R.drawable.visit2))
            }
        }

        title.text = item.name
        food_type.text = item.food_type
        phone_number.text = item.phone_number
        address = getfromlocationname(item.location.address)
        if(address == null) location.text = ""
        else    location.text = item.location.address

        val RestaurantPopup = AlertDialog.Builder(this.requireContext())
            .setCancelable(false)
            .create()

        editButton.setOnClickListener {
            editButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            editokButton.visibility = View.VISIBLE
            editcancelButton.visibility = View.VISIBLE
            askrestaurantdelete.visibility = View.GONE
            deleteokButton.visibility = View.GONE
            deletecancelButton.visibility = View.GONE

            pimage.visibility = View.GONE
            food_type.visibility = View.GONE
            phone_number.visibility = View.GONE
            location.visibility = View.GONE
            edit_name.visibility = View.VISIBLE
            edit_food_type.visibility = View.VISIBLE
            edit_phone_number.visibility = View.VISIBLE
            edit_location.visibility = View.VISIBLE

            edit_name.text = title.text
            edit_food_type.text = food_type.text
            edit_phone_number.text = phone_number.text
            edit_location.text = location.text
        }

        deleteButton.setOnClickListener {
            editButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            editokButton.visibility = View.GONE
            editcancelButton.visibility = View.GONE
            askrestaurantdelete.visibility = View.VISIBLE
            deleteokButton.visibility = View.VISIBLE
            deletecancelButton.visibility = View.VISIBLE
        }

        cancelButton.setOnClickListener {
            RestaurantPopup.dismiss()
        }

        editokButton.setOnClickListener {
            address = getfromlocationname(edit_location.text.toString())
            if(edit_name.text.toString().replace(" ", "").equals("")){
                edit_name.setError("Enter Name", )
            }
            else if(edit_food_type.text.toString().replace(" ", "").equals("")){
                edit_food_type.setError("Enter Food Type")
            }
            else if(address == null && item.restaurant_type == 2){
                Toast.makeText(this.requireContext(), "No such location", Toast.LENGTH_SHORT).show()
            }
            else{
                editButton.visibility = View.VISIBLE
                deleteButton.visibility = View.VISIBLE
                cancelButton.visibility = View.VISIBLE
                editokButton.visibility = View.GONE
                editcancelButton.visibility = View.GONE
                askrestaurantdelete.visibility = View.GONE
                deleteokButton.visibility = View.GONE
                deletecancelButton.visibility = View.GONE

                pimage.visibility = View.VISIBLE
                food_type.visibility = View.VISIBLE
                phone_number.visibility = View.VISIBLE
                location.visibility = View.VISIBLE
                edit_name.visibility = View.GONE
                edit_food_type.visibility = View.GONE
                edit_phone_number.visibility = View.GONE
                edit_location.visibility = View.GONE

                var jsonString: String = read_json()
                var newjsonString: String = "["
                val jsonArray = JSONArray(jsonString)
                var i = 0

                val newjsonObject = JSONObject()
                val newlocationjsonObject = JSONObject()

                newjsonObject.put("id", item.id)
                newjsonObject.put("name", edit_name.text)
                newjsonObject.put("food_type", edit_food_type.text)
                newjsonObject.put("restaurant_type", item.restaurant_type)
                newjsonObject.put("phone_number", edit_phone_number.text)
                if(address == null){
                    newlocationjsonObject.put("address", "")
                    newlocationjsonObject.put("latitude", 0.0)
                    newlocationjsonObject.put("longitude", 0.0)
                }
                else{
                    newlocationjsonObject.put("address", address!!.getAddressLine(0))
                    newlocationjsonObject.put("latitude", address!!.latitude)
                    newlocationjsonObject.put("longitude", address!!.longitude)
                }
                newjsonObject.put("location", newlocationjsonObject)

                while(i < jsonArray.length()){
                    val jsonObject = jsonArray.getJSONObject(i)
                    if(jsonObject.getInt("id") != item.id)  newjsonString += jsonObject.toString() + ","
                    else    newjsonString += newjsonObject.toString() + ","
                    i++
                }
                newjsonString = newjsonString.slice(IntRange(0, newjsonString.length - 2)) + "]"

                this.requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use { output ->
                    output.write(newjsonString.toByteArray())
                }
                showlist(newjsonString)

                title.text = edit_name.text
                food_type.text = edit_food_type.text
                phone_number.text = edit_phone_number.text
                if(address == null)  location.text = ""
                else    location.text =
                    getfromlocationname(address!!.getAddressLine(0))?.getAddressLine(0) ?: ""

                Toast.makeText(this.requireContext(), "EDITED", Toast.LENGTH_SHORT).show( )
            }
        }

        editcancelButton.setOnClickListener {
            editButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            editokButton.visibility = View.GONE
            editcancelButton.visibility = View.GONE
            askrestaurantdelete.visibility = View.GONE
            deleteokButton.visibility = View.GONE
            deletecancelButton.visibility = View.GONE

            pimage.visibility = View.VISIBLE
            food_type.visibility = View.VISIBLE
            phone_number.visibility = View.VISIBLE
            location.visibility = View.VISIBLE
            edit_name.visibility = View.GONE
            edit_food_type.visibility = View.GONE
            edit_phone_number.visibility = View.GONE
            edit_location.visibility = View.GONE
        }

        deleteokButton.setOnClickListener {
            var jsonString: String = read_json()
            var newjsonString: String = "["
            val jsonArray = JSONArray(jsonString)
            var i = 0

            while(i < jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(i)
                if(jsonObject.getInt("id") != item.id)  newjsonString += jsonObject.toString() + ","
                i++
            }
            if(i != 1)  newjsonString = newjsonString.slice(IntRange(0, newjsonString.length - 2)) + "]"
            else    newjsonString = "[]"

            this.requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use { output ->
                output.write(newjsonString.toByteArray())
            }
            showlist(newjsonString)

            RestaurantPopup.dismiss()

            Toast.makeText(this.requireContext(), "DELETED", Toast.LENGTH_SHORT).show()
        }

        deletecancelButton.setOnClickListener {
            editButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            editokButton.visibility = View.GONE
            editcancelButton.visibility = View.GONE
            askrestaurantdelete.visibility = View.GONE
            deleteokButton.visibility = View.GONE
            deletecancelButton.visibility = View.GONE
        }

        RestaurantPopup.setView(view)
        RestaurantPopup.show()
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
