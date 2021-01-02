package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

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
        val listOfAllImages = ImagesGallery.listOfImages(this)
        val galleryViewPagerAdapter= GalleryViewPagerAdapter(this, listOfAllImages)
        galleryViewPager.adapter = galleryViewPagerAdapter
        galleryViewPager.setCurrentItem(listOfAllImages.indexOf(path), false)
    }
}