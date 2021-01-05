package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.io.File
import java.util.ArrayList

class ImageSlider : AppCompatActivity() {
    private lateinit var backButton:ImageButton
    private lateinit var galleryViewPager:ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_slider)
        backButton = findViewById(R.id.back_button)
        galleryViewPager = findViewById(R.id.gallery_viewpager)
        backButton.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()
        val path = intent.getStringExtra("path")
        val time = intent.getStringExtra("time")
        /* To Be Modified - Retrieve images from given path*/

        val listOfAllImages = ArrayList<String>()

        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageDir = File(storageDir, "/$time")
        Log.d("@@@@@@@@@@@@@@dir",imageDir.absolutePath)
        if (imageDir.listFiles() != null) {
            for (f in imageDir.listFiles()) {
                listOfAllImages.add(f.absolutePath)
            }
        }
        val galleryViewPagerAdapter= GalleryViewPagerAdapter(this, listOfAllImages)
        galleryViewPager.adapter = galleryViewPagerAdapter
        galleryViewPager.setCurrentItem(listOfAllImages.indexOf(path), false)
    }
    private fun ViewPager.onInterceptTouchEvent(function: (MotionEvent) -> MotionEvent) {

    }
    class GalleryViewPagerAdapter(private val context: Context, private val imageList: List<String>): PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.image_page, null)
            val imageView: ImageView = view.findViewById(R.id.imageView)

            /* Add file of given position */
            val imgFile = File(imageList[position])
            val bitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageView.setImageBitmap(bitmap)
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
}