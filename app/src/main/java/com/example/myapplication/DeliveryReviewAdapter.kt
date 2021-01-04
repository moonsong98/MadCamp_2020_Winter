package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import java.io.File

class DeliveryReviewAdapter(val context: Context, val list: ArrayList<DeliveryReview>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.delivery_review_layout, parent, false)
        val deliveryReviewRestaurant: TextView = view.findViewById(R.id.delivery_review_restaurant)
        val deliveryReviewTime: TextView = view.findViewById(R.id.delivery_review_time)
        val deliveryReviewRatingBar: RatingBar = view.findViewById(R.id.delivery_review_rating_bar)
        val deliveryReviewImage: ImageView = view.findViewById(R.id.delivery_review_image)
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            Log.d("Oh", storageDir.absolutePath)
        }
        val imageDir: File = File(storageDir, "/"+list[position].timeStamp)
        Log.d("Oh", imageDir.absolutePath)
        if(imageDir.listFiles()!=null){
            val thumbnailImage = imageDir.listFiles()[0].absolutePath
            val bitmap: Bitmap = BitmapFactory.decodeFile(thumbnailImage)
            deliveryReviewImage.setImageBitmap(bitmap)
        }
        deliveryReviewRestaurant.text = list[position].restaurant
        deliveryReviewTime.text = list[position].timeStamp
        deliveryReviewRatingBar.rating = list[position].rating.toFloat()
        return view
        }
    }