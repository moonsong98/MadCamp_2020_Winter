package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [Gallery.newInstance] factory method to
 * create an instance of this fragment.
 */
class Gallery : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var galleryAdapter: GalleryAdapter
    lateinit var images:List<String>
    lateinit var gallery_number: TextView

    val MY_READ_PERMISSION_CODE: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        gallery_number = view.findViewById(R.id.gallery_number)
        recyclerView = view.findViewById(R.id.recyclerview_gallery_images)
        if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_PERMISSION_CODE)
        } else{
            loadImages();
        }
        return view
    }

    private fun loadImages() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 4)
        images = ImagesGallery.listOfImages(this.requireContext())
        galleryAdapter = GalleryAdapter(this.requireContext(), images, object: GalleryAdapter.PhotoListner{
            override fun onPhotoClick(path: String) {
            }
        })

        recyclerView.setAdapter(galleryAdapter)
        gallery_number.setText("Photos ("+images.size+")")
    }

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