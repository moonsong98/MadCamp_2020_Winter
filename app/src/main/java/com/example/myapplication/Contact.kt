package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Contact : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var restaurantlist: ListView
    private lateinit var fab: View
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_contact, container, false)
        restaurantlist = view.findViewById(R.id.restaurant_list)
        fab = view.findViewById(R.id.fab)
        searchView = view.findViewById(R.id.search_view)
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
            list.add(
                    ContactData(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("food_type"),
                            jsonObject.getInt("restaurant_type"),
                            jsonObject.getString("phone_number"),
                            jsonObject.getString("location")
                    )
            )
            i++
        }

        val adapter = ContactAdapter(this.requireContext(), list)
        restaurantlist.adapter = adapter
        restaurantlist.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as ContactData
            showRestaurantPopup(item)
        }
    }

    fun showAddPopup() {
        val inflater = this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.contact_add_popup, null)
        val addButton = view.findViewById(R.id.add_button) as Button
        val cancelButton = view.findViewById(R.id.cancel_button) as Button
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
            .create()

        cancelButton.setOnClickListener {
            AddPopup.dismiss()
        }

        addButton.setOnClickListener {
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
            else{
                restaurant_type_warn.visibility = View.GONE
                var jsonString: String = read_json()
                val jsonArray = JSONArray(jsonString)
                var new_id: Int = 1
                if(jsonArray.length() != 0) new_id = jsonArray.getJSONObject(jsonArray.length() - 1).getInt("id") + 1

                val jsonObject = JSONObject()

                jsonObject.put("id", new_id)
                jsonObject.put("name", new_name.text)
                jsonObject.put("food_type", new_food_type.text)
                jsonObject.put("restaurant_type", new_restaurant_type)
                jsonObject.put("phone_number", new_phone_number.text)
                jsonObject.put("location", new_location.text)


                if (jsonString == "[]") jsonString = "[" + jsonObject.toString() + "]"
                else    jsonString = jsonString.slice(IntRange(0, jsonString.length - 2)) + "," + jsonObject.toString() + "]"

                this.requireContext().openFileOutput("contacts.json", Context.MODE_PRIVATE).use { output ->
                    output.write(jsonString.toByteArray())
                }
                showlist(jsonString)

                AddPopup.dismiss()
                Toast.makeText(this.requireContext(), "SUCCESS", Toast.LENGTH_SHORT).show()
            }
        }

        AddPopup.setView(view)
        AddPopup.show()
    }

    fun showRestaurantPopup(item: ContactData) {
        val inflater = this.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.contact_restaurant_popup, null)
        val editButton = view.findViewById(R.id.edit_button) as ImageButton
        val deleteButton = view.findViewById(R.id.delete_button) as ImageButton
        val cancelButton = view.findViewById(R.id.cancel_button) as ImageButton
        var name = view.findViewById<TextView>(R.id.nname)
        var food_type = view.findViewById<TextView>(R.id.nfood_type)
        var phone_number = view.findViewById<TextView>(R.id.nphone_number)
        var location = view.findViewById<TextView>(R.id.nlocation)

        name.text = item.name
        food_type.text = item.food_type
        phone_number.text = item.phone_number
        location.text = item.location

        val RestaurantPopup = AlertDialog.Builder(this.requireContext())
                .setTitle(name.text)
                .create()

        editButton.setOnClickListener {
            editButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
            cancelButton.visibility = View.VISIBLE
        }

        deleteButton.setOnClickListener {

        }

        cancelButton.setOnClickListener {
            editButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
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