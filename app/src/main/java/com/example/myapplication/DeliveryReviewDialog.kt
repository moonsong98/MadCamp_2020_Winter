package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.ArrayList

class DeliveryReviewDialog(
    private val context: Context,
    private var deliveryReview: DeliveryReview,
) {
    lateinit var restaurant: TextView
    lateinit var images: ArrayList<String>
    lateinit var reviewDialogRecyclerView: RecyclerView
    lateinit var reviewDialogGalleryAdapter: GalleryAdapter
    lateinit var popup: AlertDialog
    lateinit var reviewDialogRatingBar: RatingBar
    lateinit var reviewDialogReview: TextView

    lateinit var review_dialog_restaurant_list_edit: Spinner
    lateinit var review_dialog_review_edit: EditText
    lateinit var review_dialog_add_photo: ImageButton
    lateinit var spinnerAdapter: AddDeliveryReviewDialog.CustomAdapter

    lateinit var editButton: ImageButton
    lateinit var cancelButton: ImageButton
    lateinit var removeButton: ImageButton

    var restaurantList: ArrayList<String> = ArrayList<String>()
    var editMode: Boolean = false

    val reviewTime = deliveryReview.timeStamp
    fun createAddDeliveryReviewDialog(
        removeDeliveryReview: (deliveryReview: DeliveryReview) -> Unit,
        reloadReviewList: () -> Unit
    ) {
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.delivery_review_dialog, null)
        /* Original view */
        restaurant = view.findViewById(R.id.review_dialog_restaurant_list)
        restaurant.text = deliveryReview.restaurant
        reviewDialogRatingBar = view.findViewById(R.id.review_dialog_rating_bar)
        reviewDialogRatingBar.rating = deliveryReview.rating.toFloat()
        reviewDialogReview = view.findViewById(R.id.review_dialog_review)
        reviewDialogReview.text = deliveryReview.review
        /* Edit view */
        review_dialog_restaurant_list_edit = view.findViewById(R.id.review_dialog_restaurant_list_edit)
        review_dialog_review_edit = view.findViewById(R.id.review_dialog_review_edit)
        review_dialog_add_photo = view.findViewById(R.id.review_dialog_add_photo)
        editButton = view.findViewById(R.id.edit_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        removeButton = view.findViewById(R.id.remove_button)

        /* Retrieve Images */
        images = ArrayList<String>()

        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageDir = File(storageDir, "/" + deliveryReview.timeStamp)
        if (imageDir.listFiles() != null) {
            for (f in imageDir.listFiles()) {
                images.add(f.absolutePath)
            }
        }
        /* Create Dialog */
        /* Setup Dialog Components */
        reviewDialogRecyclerView = view.findViewById(R.id.review_dialog_recycler_view)
        reviewDialogRecyclerView.setHasFixedSize(true)
        reviewDialogRecyclerView.layoutManager = GridLayoutManager(context, 4)
        popup = AlertDialog.Builder(this.context)
            .setTitle("Deliver Review")
            .create()
        editButton.setOnClickListener {
            setMode()
            if (!editMode) {
                var json = "[]"
                try {
                    val inputStream = this.context.openFileInput("delivery_review.json")
                    json = inputStream.bufferedReader().use { it.readText() }

                } catch (ex: Exception) {
                    Log.d("Oh", "Exception Occurred")
                    ex.printStackTrace()
                }
                if (json == "")
                    json = "[]"
                var newJson= "["
                val jsonArray = JSONArray(json)
                val newJsonObject = JSONObject()

                newJsonObject.put("restaurant",
                    review_dialog_restaurant_list_edit.selectedItem.toString())
                newJsonObject.put("rating", reviewDialogRatingBar.rating)
                newJsonObject.put("review", review_dialog_review_edit.text)
                newJsonObject.put("timeStamp", deliveryReview.timeStamp)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    newJson +=
                        if (jsonObject.getString("timeStamp") != deliveryReview.timeStamp) "$jsonObject,"
                        else "$newJsonObject,"
                }
                newJson = newJson.slice(IntRange(0, newJson.length - 2)) + "]"

                this.context.openFileOutput("delivery_review.json", Context.MODE_PRIVATE)
                    .use { output ->
                        output.write(newJson.toByteArray())
                    }
                restaurant.text = review_dialog_restaurant_list_edit.selectedItem.toString()
                reviewDialogReview.text = review_dialog_review_edit.text

                deliveryReview.restaurant = review_dialog_restaurant_list_edit.selectedItem.toString()
                deliveryReview.rating = reviewDialogRatingBar.rating.toInt()
                deliveryReview.review = review_dialog_review_edit.text.toString()
            }
        }
        removeButton.setOnClickListener {
            var json = "[]"
            try {
                val inputStream = this.context.openFileInput("delivery_review.json")
                json = inputStream.bufferedReader().use { it.readText() }

            } catch (ex: Exception) {
                Log.d("Oh", "Exception Occurred")
                ex.printStackTrace()
            }
            if (json == "")
                json = "[]"

            var newjsonString: String = "["
            val jsonArray = JSONArray(json)

            if (jsonArray.length() > 1) {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    if (jsonObject.getString("timeStamp") != deliveryReview.timeStamp) newjsonString += jsonObject.toString() + ","
                }
                newjsonString = newjsonString.slice(IntRange(0, newjsonString.length - 2)) + "]"
            } else {
                newjsonString = "[]"
            }

            context.openFileOutput("delivery_review.json", Context.MODE_PRIVATE).use { output ->
                output.write(newjsonString.toByteArray())
            }
            removeDeliveryReview(deliveryReview)
            popup.dismiss()
        }
        cancelButton.setOnClickListener {
            reloadReviewList()
            popup.dismiss()
        }
        popup.setView(view)
    }

    private fun setMode() {
        if (!editMode) {
            review_dialog_restaurant_list_edit.visibility = VISIBLE
            updateRestaurantSpinner()
            val restaurantIndex = spinnerAdapter.getPosition(restaurant.text.toString())
            review_dialog_restaurant_list_edit.setSelection(restaurantIndex)
            review_dialog_review_edit.visibility = VISIBLE
            review_dialog_review_edit.setText(reviewDialogReview.text.toString())
            review_dialog_add_photo.visibility = VISIBLE
            restaurant.visibility = GONE
            reviewDialogReview.visibility = GONE
            reviewDialogRatingBar.setIsIndicator(false)
            editButton.setImageResource(R.drawable.ok)
            editMode = true
        } else {
            review_dialog_restaurant_list_edit.visibility = GONE
            review_dialog_review_edit.visibility = GONE
            review_dialog_add_photo.visibility = GONE
            restaurant.visibility = VISIBLE
            reviewDialogReview.visibility = VISIBLE
            reviewDialogRatingBar.setIsIndicator(true)
            editButton.setImageResource(R.drawable.edit)
            editMode = false
        }
    }

    private fun updateRestaurantSpinner() {
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
        while (i < jsonArray.length()) {
            val res = jsonArray.getJSONObject(i).getString("name")
            val type = jsonArray.getJSONObject(i).getInt("restaurant_type")
            if (!restaurantList.contains(res) && type == 1)
                restaurantList.add(res)
            i++
        }
        spinnerAdapter = AddDeliveryReviewDialog.CustomAdapter(this.context,
            R.layout.support_simple_spinner_dropdown_item,
            restaurantList)
        review_dialog_restaurant_list_edit.adapter = spinnerAdapter
    }

    fun popup() {
        popup.show()
    }
}