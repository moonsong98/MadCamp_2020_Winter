package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.util.ArrayList

class AddDeliveryReviewDialog(private val context: Context) {
    lateinit var images: ArrayList<String>
    lateinit var reviewDialogRecyclerView: RecyclerView
    lateinit var reviewDialogGalleryAdapter: GalleryAdapter
    lateinit var popup: AlertDialog
    lateinit var reviewDialogAddPhoto: ImageButton
    lateinit var reviewDialogRestaurantList: Spinner
    fun createAddDeliveryReviewDialog() {
        val inflater =
            this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.add_review_dialog, null)
        reviewDialogRestaurantList =
            view.findViewById(R.id.review_dialog_restaurant_list)
        val reviewDialogRatingBar: RatingBar = view.findViewById(R.id.review_dialog_rating_bar)
        val reviewDialogReview: EditText = view.findViewById(R.id.review_dialog_review)

        reviewDialogAddPhoto = view.findViewById(R.id.review_dialog_add_photo)
        reviewDialogRecyclerView = view.findViewById(R.id.review_dialog_recycler_view)
        images = ArrayList<String>()
        /* Create Dialog */
        popup = AlertDialog.Builder(this.context)
            .setTitle("Add Deliver Review")
            .setPositiveButton("ADD", null)
            .setNegativeButton("Cancel", null)
            .create()
        popup.setView(view)

        /* Setup Dialog Componenets */
        var editMode: Boolean = false

        reviewDialogRecyclerView.setHasFixedSize(true)
        reviewDialogRecyclerView.layoutManager = GridLayoutManager(context, 4)

    }

    fun showPopup() {
        popup.show()
    }

    fun dismissPopup() {
        popup.dismiss()
    }

    fun updateRestaurantSpinner() {
        var restaurantList: ArrayList<String> = ArrayList<String>()
        var json: String = "[]"
        try {
            val inputStream = this.context.openFileInput("contacts.json")
            json = inputStream.bufferedReader().use { it.readText() }

        } catch (ex: Exception) {
            Log.d("Oh", "Exception Occurred")
            ex.printStackTrace()
        }
        if (json == "")
            json = "[]"

        var i = 0
        val jsonArray = JSONArray(json)
        restaurantList.add("Choose Restaurant...")
        while(i<jsonArray.length()){
            val res = jsonArray.getJSONObject(i).getString("name")
            if (!restaurantList.contains(res))
                restaurantList.add(res)
            i++
        }
        val spinnerAdapter = CustomAdapter(this.context, R.layout.support_simple_spinner_dropdown_item,restaurantList)
        reviewDialogRestaurantList.adapter = spinnerAdapter
    }

    class CustomAdapter(context: Context, resource: Int, list: ArrayList<String>): ArrayAdapter<String>(context, resource, list) {
        override fun isEnabled(position: Int): Boolean {
            return position != 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view:View = super.getView(position, convertView, parent)
            val tv: TextView = view as TextView
            if(position == 0) tv.setTextColor(Color.GRAY) else tv.setTextColor(Color.BLACK)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = super.getDropDownView(position, convertView, parent)
            val tv:TextView = view as TextView
            if(position == 0) tv.setTextColor(Color.GRAY) else tv.setTextColor(Color.BLACK)
            return view
        }
    }
}