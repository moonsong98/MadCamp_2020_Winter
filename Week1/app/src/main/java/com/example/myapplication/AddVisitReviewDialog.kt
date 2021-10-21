package com.example.myapplication

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
import org.json.JSONObject
import java.util.ArrayList

class AddVisitReviewDialog(private val context: Context) {
    lateinit var images: ArrayList<String>
    lateinit var reviewDialogRecyclerView: RecyclerView
    lateinit var reviewDialogGalleryAdapter: GalleryAdapter
    lateinit var popup: AlertDialog
    lateinit var reviewDialogAddPhoto: ImageButton
    lateinit var reviewDialogRestaurantList: Spinner
    lateinit var timeStampReviewAdded: String
    lateinit var reviewDialogRatingBar: RatingBar
    lateinit var editButton: ImageView
    lateinit var reviewDialogReview: EditText

    lateinit var addButton: ImageButton
    lateinit var cancelButton: ImageButton
    var editMode: Boolean = false
    fun createAddDeliveryReviewDialog(callback:(visitReview:VisitReviewData)->Unit) {
        val inflater =
            this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.add_visit_review_dialog, null)
        reviewDialogRestaurantList =
            view.findViewById(R.id.review_dialog_restaurant_list)
        reviewDialogRatingBar = view.findViewById(R.id.review_dialog_rating_bar)
        reviewDialogReview = view.findViewById(R.id.review_dialog_review)
        var visitReview: VisitReviewData

        reviewDialogAddPhoto = view.findViewById(R.id.review_dialog_add_photo)
        editButton = view.findViewById(R.id.review_edit_photo)
        editButton.isClickable = true
        editButton.setOnClickListener() {
            editMode = true
            editButton.visibility = View.INVISIBLE
            reviewDialogAddPhoto.setImageResource(R.drawable.cancel)
        }
        reviewDialogRecyclerView = view.findViewById(R.id.review_dialog_recycler_view)
        images = ArrayList<String>()

        addButton = view.findViewById(R.id.add_button)
        addButton.isClickable = true
        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.isClickable = true

        /* Create Dialog */
        /* Setup Dialog Components */
        reviewDialogRecyclerView.setHasFixedSize(true)
        reviewDialogRecyclerView.layoutManager = GridLayoutManager(context, 4)
        popup = AlertDialog.Builder(this.context)
            .setTitle("Add Deliver Review")
            .setCancelable(false)
            .create()
        popup.setView(view)

        addButton.setOnClickListener{
            if(reviewDialogRestaurantList.selectedItemPosition == 0) {
                val warningMessage: TextView = view.findViewById(R.id.warning_message)
                warningMessage.setTextColor(Color.RED)
                warningMessage.visibility = View.VISIBLE
            } else {
                val restaurant: String = reviewDialogRestaurantList.selectedItem.toString()
                val rating: Int = reviewDialogRatingBar.rating.toInt()
                val review: String = reviewDialogReview.text.toString()
                visitReview = VisitReviewData(restaurant, rating, review, timeStampReviewAdded)
                callback(visitReview)

                var json = "[]"
                try {
                    val inputStream = context.openFileInput("visit_review.json")
                    json = inputStream.bufferedReader().use { it.readText() }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                json = if(json == "") "[]" else json
                val jsonArray = JSONArray(json)
                val jsonObject = JSONObject()
                jsonObject.put("restaurant", restaurant)
                jsonObject.put("rating", rating)
                jsonObject.put("review", review)
                jsonObject.put("timeStamp", timeStampReviewAdded)

                if(json == "[]") json = "[$jsonObject]"
                else json = json.slice(IntRange(0, json.length - 2)) + "," + jsonObject.toString() + "]"
                context.openFileOutput("visit_review.json", Context.MODE_PRIVATE).use { output ->
                    output.write(json.toByteArray())
                }
                val warningMessage: TextView = view.findViewById(R.id.warning_message)
                warningMessage.visibility = View.INVISIBLE
                popup.dismiss()
            }
        }
        cancelButton.setOnClickListener {
            val warningMessage: TextView = view.findViewById(R.id.warning_message)
            warningMessage.visibility = View.INVISIBLE
            popup.dismiss()
        }
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
            val type = jsonArray.getJSONObject(i).getInt("restaurant_type")
            if (!restaurantList.contains(res) && type == 2)
                restaurantList.add(res)
            i++
        }
        val spinnerAdapter = CustomAdapter(this.context, R.layout.support_simple_spinner_dropdown_item,restaurantList)
        reviewDialogRestaurantList.adapter = spinnerAdapter
    }

    fun setReviewAddedTime(time:String) {
        timeStampReviewAdded = time
    }

    fun resetDialog() {
        reviewDialogRatingBar.rating = 0F
        reviewDialogReview.setText("")
        updateRestaurantSpinner()
    }

    class CustomAdapter(context: Context, resource: Int, list: ArrayList<String>): ArrayAdapter<String>(context, resource, list) {
        override fun isEnabled(position: Int): Boolean {
            return position != 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = super.getView(position, convertView, parent)
            val tv: TextView = view as TextView
            if(position == 0) tv.setTextColor(Color.GRAY) else tv.setTextColor(Color.BLACK)
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View = super.getDropDownView(position, convertView, parent)
            val tv: TextView = view as TextView
            if(position == 0) tv.setTextColor(Color.GRAY) else tv.setTextColor(Color.BLACK)
            return view
        }
    }
}