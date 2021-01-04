package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.LAYOUT_INFLATER_SERVICE
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

/**
 * A simple [Fragment] subclass.
 * Use the [Gallery.newInstance] factory method to
 * create an instance of this fragment.
 */
class Gallery : Fragment() {
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var galleryViewPagerAdapter: GalleryViewPagerAdapter
    private lateinit var images:List<String>
    private lateinit var galleryNumber: TextView
    private lateinit var title: RelativeLayout
    private lateinit var addReview: ImageButton
    private lateinit var reviewList: ListView
    private var reviewListData: ArrayList<DeliveryReview> = ArrayList<DeliveryReview>()
    private lateinit var addDeliveryReviewDialog: AddDeliveryReviewDialog
    private var takenPhoto: File? = null
    /* Review Storage */
    private lateinit var reviewTimeStamp:String
    private var reviewImagesStorage: File = File("")

    enum class ModifyState{
        ADD, DELETE, CLEAR
    }

    companion object {
        private val REQUEST_READ_STORACE: Int = 101
        private val REQUEST_TAKE_PHOTO: Int = 1
        private val TAKE_PHOTO: Int = 1
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        galleryNumber = view.findViewById(R.id.gallery_number)
        title = view.findViewById(R.id.general_photos)
        reviewList = view.findViewById(R.id.review_list)
        val deliveryReviewAdapter = DeliveryReviewAdapter(this.requireContext(), reviewListData)
        reviewList.adapter = deliveryReviewAdapter


        /*Add Photo Button*/
        addDeliveryReviewDialog  = AddDeliveryReviewDialog(this.requireContext())
        addDeliveryReviewDialog.createAddDeliveryReviewDialog { deliveryReview -> updateReviewList(deliveryReview) }

        addReview = view.findViewById(R.id.add_review)
        addReview.isClickable = true
        addReview.setOnClickListener {
            reviewTimeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            addDeliveryReviewDialog.updateRestaurantSpinner()
            addDeliveryReviewDialog.showPopup()
            addDeliveryReviewDialog.setReviewAddedTime(reviewTimeStamp)
            addDeliveryReviewDialog.reviewDialogRatingBar.rating = 0F
            updateAddDeliveryReviewDialogImages(addDeliveryReviewDialog.reviewDialogGalleryAdapter, addDeliveryReviewDialog.images, "", ModifyState.CLEAR)
        }
        setDeliveryAddDialogPhoto()
        setReviewDialogGalleryAdapter()

        /* Request Permission To Read External Storage */
        if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORACE)
        } else{
            loadImages();
        }

        jsonResult(readJson())

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private fun loadImages() {
        val activity = this.activity
        images = ImagesGallery.listOfImages(this.requireContext(), reviewImagesStorage)
        galleryAdapter = GalleryAdapter(this.requireContext(), images, object: GalleryAdapter.PhotoListener{
            override fun onPhotoClick(path: String) {
                val intent = Intent(activity, ImageSlider::class.java).apply{
                    putExtra("path", path)
                }
                startActivity(intent)
            }
        })
        galleryAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        galleryNumber.text = "General ("+images.size+")"
        /* Gallery View Pager Adapter */
        galleryViewPagerAdapter = GalleryViewPagerAdapter(this.requireContext(), images)
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
//                reviewTimeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
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
                if(resultCode == RESULT_OK) {
                    updateAddDeliveryReviewDialogImages(addDeliveryReviewDialog.reviewDialogGalleryAdapter,addDeliveryReviewDialog.images,takenPhoto!!.absolutePath, ModifyState.ADD)
                } else {
                    takenPhoto?.delete()
                }
            }
            addDeliveryReviewDialog.showPopup()
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName:String = "JPEG_" + timeStamp + "_"
        val storageDir: File? = this.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageDir: File = File(storageDir, "/"+reviewTimeStamp)
        reviewImagesStorage = imageDir
        if(!imageDir.isDirectory) {
            imageDir.mkdirs()
        }
        return File.createTempFile(
                imageFileName,
                ".jpg",
                 imageDir
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
    private fun updateAddDeliveryReviewDialogImages(galleryAdapter:GalleryAdapter,  images:ArrayList<String>, imageToBeAdded:String, modifyState: Gallery.ModifyState){
        when (modifyState) {
            ModifyState.ADD -> run {
                val len = images.size
                images.add(len, imageToBeAdded)
                galleryAdapter.notifyItemInserted(len)
            }
            ModifyState.DELETE -> {
                TODO("Not yet implemented")
            }
            ModifyState.CLEAR -> {
                images.clear()
                galleryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setDeliveryAddDialogPhoto () {
        addDeliveryReviewDialog.reviewDialogAddPhoto.isClickable = true
        addDeliveryReviewDialog.reviewDialogAddPhoto.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
                addDeliveryReviewDialog.dismissPopup()
            } else{
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Gallery.REQUEST_TAKE_PHOTO)
            }
        }

    }

    private fun setReviewDialogGalleryAdapter() {
        addDeliveryReviewDialog.reviewDialogGalleryAdapter = GalleryAdapter(this.requireContext(), addDeliveryReviewDialog.images, object: GalleryAdapter.PhotoListener{
            override fun onPhotoClick(path: String) {
                val intent = Intent(activity, ImageSlider::class.java).apply{
                    putExtra("path", path)
                }
                startActivity(intent)
            }
        })
        addDeliveryReviewDialog.reviewDialogRecyclerView.adapter = addDeliveryReviewDialog.reviewDialogGalleryAdapter

    }

    private fun updateReviewList(deliveryReview:DeliveryReview) {
        reviewListData.add(deliveryReview)
        val deliveryReviewAdapter = DeliveryReviewAdapter(this.requireContext(), reviewListData)
        reviewList.adapter = deliveryReviewAdapter
    }

    private fun readJson():String {
        var json = "[]"
        try {
            val inputStream = this.requireContext().openFileInput("delivery_review.json")
            json = inputStream.bufferedReader().use { it.readText() }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return if(json == "") "[]" else json
    }
    private fun jsonResult(string: String) {
        val jsonArray = JSONArray(string)
        for(index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            reviewListData.add(
                DeliveryReview(
                    jsonObject.getString("restaurant"),
                    jsonObject.getInt("rating"),
                    jsonObject.getString("review"),
                    jsonObject.getString("timeStamp")
                )
            )
        }
        val deliveryReviewAdapter = DeliveryReviewAdapter(this.requireContext(), reviewListData)
        reviewList.adapter = deliveryReviewAdapter
    }
}