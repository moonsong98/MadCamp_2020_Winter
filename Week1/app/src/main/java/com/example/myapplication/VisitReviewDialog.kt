package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.ArrayList

class VisitReviewDialog(
    private val context: Context,
    private var visitReviewData: VisitReviewData
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

    lateinit var reviewDialogLocation: TextView
    lateinit var editButton: ImageButton
    lateinit var cancelButton: ImageButton
    lateinit var removeButton: ImageButton
    lateinit var editCancelButton: ImageButton
    lateinit var deleteOkButton: ImageButton
    lateinit var deleteCancelButton: ImageButton
    lateinit var deleteAskText: TextView

    lateinit var address:String

    var restaurantList: ArrayList<String> = ArrayList<String>()
    var editMode: Boolean = false

    val reviewTime = visitReviewData.timeStamp
    fun createAddDeliveryReviewDialog(
        removeVisitReview: (visitReviewData: VisitReviewData) -> Unit,
        reloadReviewList: () -> Unit
    ) {
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.visit_review_dialog, null)
        /* Original view */
        restaurant = view.findViewById(R.id.review_dialog_restaurant_list)
        restaurant.text = visitReviewData.restaurant
        reviewDialogRatingBar = view.findViewById(R.id.review_dialog_rating_bar)
        reviewDialogRatingBar.rating = visitReviewData.rating.toFloat()
        reviewDialogReview = view.findViewById(R.id.review_dialog_review)
        reviewDialogReview.text = visitReviewData.review
        /* Edit view */
        review_dialog_restaurant_list_edit = view.findViewById(R.id.review_dialog_restaurant_list_edit)
        review_dialog_review_edit = view.findViewById(R.id.review_dialog_review_edit)
        review_dialog_add_photo = view.findViewById(R.id.review_dialog_add_photo)
        reviewDialogLocation = view.findViewById(R.id.review_dialog_location)
        editButton = view.findViewById(R.id.edit_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        removeButton = view.findViewById(R.id.remove_button)
        editCancelButton = view.findViewById(R.id.edit_cancel_button)
        deleteAskText = view.findViewById(R.id.ask_delete)
        deleteOkButton = view.findViewById(R.id.delete_ok_button)
        deleteCancelButton = view.findViewById(R.id.delete_cancel_button)

        /* Retrieve Images */
        images = ArrayList<String>()

        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageDir = File(storageDir, "/visit/" + visitReviewData.timeStamp)
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
        viewLocation()

        popup = AlertDialog.Builder(this.context)
            .setCancelable(false)
            .create()
        editButton.setOnClickListener {
            if (editMode) {
                if(review_dialog_restaurant_list_edit.selectedItemPosition == 0) {
                    val warningMessage: TextView = view.findViewById(R.id.warning_message)
                    warningMessage.visibility = View.VISIBLE
                    warningMessage.setTextColor(Color.RED)
                } else {
                    var json = "[]"
                    try {
                        val inputStream = this.context.openFileInput("visit_review.json")
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
                    newJsonObject.put("timeStamp", visitReviewData.timeStamp)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        newJson +=
                            if (jsonObject.getString("timeStamp") != visitReviewData.timeStamp) "$jsonObject,"
                            else "$newJsonObject,"
                    }
                    newJson = newJson.slice(IntRange(0, newJson.length - 2)) + "]"

                    this.context.openFileOutput("visit_review.json", Context.MODE_PRIVATE)
                        .use { output ->
                            output.write(newJson.toByteArray())
                        }
                    restaurant.text = review_dialog_restaurant_list_edit.selectedItem.toString()
                    reviewDialogReview.text = review_dialog_review_edit.text

                    visitReviewData.restaurant = review_dialog_restaurant_list_edit.selectedItem.toString()
                    visitReviewData.rating = reviewDialogRatingBar.rating.toInt()
                    visitReviewData.review = review_dialog_review_edit.text.toString()
                    val warningMessage: TextView = view.findViewById(R.id.warning_message)
                    warningMessage.visibility = View.INVISIBLE
                    viewLocation()
                    setMode()
                }
            }
            else {
                viewLocation()
                setMode()
            }
        }

        removeButton.setOnClickListener {
            editButton.visibility = View.GONE
            removeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            editCancelButton.visibility = View.GONE
            deleteAskText.visibility = View.VISIBLE
            deleteOkButton.visibility = View.VISIBLE
            deleteCancelButton.visibility = View.VISIBLE
        }

        deleteOkButton.setOnClickListener {
            var json = "[]"
            try {
                val inputStream = this.context.openFileInput("visit_review.json")
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
                    if (jsonObject.getString("timeStamp") != visitReviewData.timeStamp) newjsonString += jsonObject.toString() + ","
                }
                newjsonString = newjsonString.slice(IntRange(0, newjsonString.length - 2)) + "]"
            } else {
                newjsonString = "[]"
            }

            context.openFileOutput("visit_review.json", Context.MODE_PRIVATE).use { output ->
                output.write(newjsonString.toByteArray())
            }
            removeVisitReview(visitReviewData)
            popup.dismiss()
        }

        deleteCancelButton.setOnClickListener {
            editButton.visibility = View.VISIBLE
            removeButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            editCancelButton.visibility = View.GONE
            deleteAskText.visibility = View.GONE
            deleteOkButton.visibility = View.GONE
            deleteCancelButton.visibility = View.GONE
        }

        cancelButton.setOnClickListener {
            reloadReviewList()
            popup.dismiss()
        }

        editCancelButton.setOnClickListener {
            setMode()
        }

        popup.setView(view)
    }

    private fun viewLocation() {
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
        while (i < jsonArray.length()) {
            val res = jsonArray.getJSONObject(i).getString("name")
            val type = jsonArray.getJSONObject(i).getInt("restaurant_type")
            if (res == restaurant.text && type == 2){
                reviewDialogLocation.text = jsonArray.getJSONObject(i).getJSONObject("location").getString("address")
                break
            }
            i++
        }
    }

    private fun setMode() {
        if (!editMode) {
            review_dialog_restaurant_list_edit.visibility = View.VISIBLE
            updateRestaurantSpinner()
            val restaurantIndex = spinnerAdapter.getPosition(restaurant.text.toString())
            review_dialog_restaurant_list_edit.setSelection(restaurantIndex)
            review_dialog_review_edit.visibility = View.VISIBLE
            review_dialog_review_edit.setText(reviewDialogReview.text.toString())
            review_dialog_add_photo.visibility = View.VISIBLE
            restaurant.visibility = View.GONE
            reviewDialogReview.visibility = View.GONE
            reviewDialogRatingBar.setIsIndicator(false)
            reviewDialogLocation.visibility = View.GONE
            editButton.setImageResource(R.drawable.ok)
            removeButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            editCancelButton.visibility = View.VISIBLE
            editMode = true
        } else {
            review_dialog_restaurant_list_edit.visibility = View.GONE
            review_dialog_review_edit.visibility = View.GONE
            review_dialog_add_photo.visibility = View.GONE
            restaurant.visibility = View.VISIBLE
            reviewDialogReview.visibility = View.VISIBLE
            reviewDialogRatingBar.setIsIndicator(true)
            reviewDialogLocation.visibility = View.VISIBLE
            editButton.setImageResource(R.drawable.edit)
            removeButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            editCancelButton.visibility = View.GONE
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
        restaurantList = ArrayList()
        restaurantList.add("Choose Restaurant...")
        while (i < jsonArray.length()) {
            val res = jsonArray.getJSONObject(i).getString("name")
            val type = jsonArray.getJSONObject(i).getInt("restaurant_type")
            if (!restaurantList.contains(res) && type == 2)
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

    fun dismiss() {
        popup.dismiss()
    }
}