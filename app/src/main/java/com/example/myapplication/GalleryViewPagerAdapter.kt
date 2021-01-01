package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.io.File

class GalleryViewPagerAdapter(private val context: Context, private val imageList: List<String>, private val callback:()->(Unit)): PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View = inflater.inflate(R.layout.image_page, null)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        /* Add file of given position */
        val imgFile = File(imageList[position])
        val bitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
        imageView.setImageBitmap(bitmap)
        imageView.setOnClickListener{
            callback()
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int = imageList.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object`as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return(view == `object` as View)
    }
}