package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [Gallery.newInstance] factory method to
 * create an instance of this fragment.
 */
class Gallery : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var galleryViewPagerAdapter: GalleryViewPagerAdapter
    private lateinit var images:List<String>
    private lateinit var galleryNumber: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var title: LinearLayout

    companion object {
        private val MY_READ_PERMISSION_CODE: Int = 101
    }
    fun closeSlide() {
        recyclerView.visibility = VISIBLE
        viewPager.visibility = INVISIBLE
        title.visibility = VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        galleryNumber = view.findViewById(R.id.gallery_number)
        recyclerView = view.findViewById(R.id.recyclerview_gallery_images)
        title = view.findViewById(R.id.general_photos)
        viewPager = view.findViewById(R.id.gallery_viewpager)
        viewPager.clipToPadding = false
        val density: Float = resources.displayMetrics.density
        val margin: Int = (24*density).toInt()
        viewPager.setPadding(margin, 0, margin, 0)
        viewPager.pageMargin = margin/2
        if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_PERMISSION_CODE)
        } else{
            loadImages();
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_PERMISSION_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun loadImages() {
        val context: Context = this.requireContext()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 3)
        images = ImagesGallery.listOfImages(this.requireContext())
        galleryAdapter = GalleryAdapter(this.requireContext(), images, object: GalleryAdapter.PhotoListener{
            override fun onPhotoClick(path: String) {
                viewPager.setCurrentItem(images.indexOf(path),false)
                recyclerView.visibility = INVISIBLE
                viewPager.visibility = VISIBLE
                title.visibility = INVISIBLE
            }
        })
        galleryViewPagerAdapter = GalleryViewPagerAdapter(this.requireContext(), images) { closeSlide() }
        recyclerView.adapter = galleryAdapter
        viewPager.adapter = galleryViewPagerAdapter
        galleryNumber.text = "General ("+images.size+")"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == MY_READ_PERMISSION_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this.requireContext(), "Read external storage permission granted", Toast.LENGTH_SHORT).show()
                loadImages()
            } else {
                Toast.makeText(this.requireContext(), "Read external storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}