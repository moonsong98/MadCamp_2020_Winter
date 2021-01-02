package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var title: RelativeLayout
    private lateinit var addPhotoButton: ImageButton
    private var takenPhoto: File? = null
    private lateinit var minflater:LayoutInflater
    private var mcontainer:ViewGroup? = null

    companion object {
        private val REQUEST_READ_STORACE: Int = 101
        private val REQUEST_TAKE_PHOTO: Int = 1
        private val TAKE_PHOTO: Int = 1
    }
    private fun closeSlide() {
        recyclerView.visibility = VISIBLE
        viewPager.visibility = GONE
        title.visibility = VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        minflater = inflater
        mcontainer = container
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        galleryNumber = view.findViewById(R.id.gallery_number)
        recyclerView = view.findViewById(R.id.recyclerview_gallery_images)
        title = view.findViewById(R.id.general_photos)
        viewPager = view.findViewById(R.id.gallery_viewpager)

        /*Add Photo Button*/
        addPhotoButton = view.findViewById(R.id.add_photo)
        addPhotoButton.isClickable = true
        addPhotoButton.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else{
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_TAKE_PHOTO)
            }
        }

        /* Photo Swipe */
        viewPager.clipToPadding = false
        val density: Float = resources.displayMetrics.density
        val margin: Int = (24*density).toInt()
        viewPager.setPadding(margin, 0, margin, 0)
        viewPager.pageMargin = margin/2

        /* Request Permission To Read External Storage */
        if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORACE)
        } else{
            loadImages();
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun loadImages() {
        val activity = this.activity
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        images = ImagesGallery.listOfImages(this.requireContext())
        galleryAdapter = GalleryAdapter(this.requireContext(), images, object: GalleryAdapter.PhotoListener{
            override fun onPhotoClick(path: String) {
                val intent = Intent(activity, ImageSlider::class.java).apply{
                    putExtra("path", path)
                }
                startActivity(intent)
            }
        })
        galleryAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        recyclerView.adapter = galleryAdapter
        galleryNumber.text = "General ("+images.size+")"
        /* Gallery View Pager Adapter */
        galleryViewPagerAdapter = GalleryViewPagerAdapter(this.requireContext(), images)
        viewPager.adapter = galleryViewPagerAdapter
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_READ_STORACE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages()
            }
        }
        if(requestCode == REQUEST_TAKE_PHOTO) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == TAKE_PHOTO) {
            if (data != null) {
                if(resultCode == RESULT_OK) loadImages() else takenPhoto?.delete()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile():File {
        val timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName:String = "JPEG_" + timeStamp + "_"
        val storageDir: File? = this.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try{
            photoFile = createImageFile()
        } catch(ex:IOException){}
        if(photoFile != null) run {
            val photoURI: Uri = FileProvider.getUriForFile(this.requireContext(), "com.example.myapplication.fileProvider",photoFile)
            takenPhoto = photoFile
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

}