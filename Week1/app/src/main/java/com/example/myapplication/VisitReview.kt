package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONArray
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VisitReview : Fragment(), OnMapReadyCallback {
    lateinit var googleMap: GoogleMap
    lateinit var mapView: MapView
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1

    lateinit var addReviewButton: TextView
    lateinit var reviewList: ListView
    private var reviewListData: ArrayList<VisitReviewData> = ArrayList()
    private lateinit var addVisitReviewDialog: AddVisitReviewDialog
    private lateinit var visitReviewDialog: VisitReviewDialog
    private var takenPhoto: File? = null

    /*Review Storage*/
    private lateinit var reviewTimeStamp: String
    private var reviewImagesStorage: File = File("")
    private lateinit var visitReviewAdapter: VisitReviewAdapter

    enum class ModifyState {
        ADD, DELETE, CLEAR
    }

    companion object {
        private const val REQUEST_READ_STORAGE: Int = 101
        private const val REQUEST_TAKE_PHOTO_ADD_REVIEW: Int = 1
        private const val REQUEST_TAKE_PHOTO_REVIEW: Int = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_visitreview, container, false)
        reviewList = view.findViewById(R.id.review_list)
        addReviewButton = view.findViewById(R.id.add_review)
        visitReviewAdapter = VisitReviewAdapter(this.requireContext(), reviewListData)
        val context = this.requireContext()
        reviewList.adapter = visitReviewAdapter
        reviewList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                visitReviewDialog = VisitReviewDialog(
                    context,
                    parent.getItemAtPosition(position) as VisitReviewData
                )
                visitReviewDialog.createAddDeliveryReviewDialog({ visitReviewData ->
                    removeReview(visitReviewData)
                }, { reloadReviewList() })
                setVisitReviewDialogGalleryAdapter()
                setDialogAddPhoto(REQUEST_TAKE_PHOTO_REVIEW)
                visitReviewDialog.popup()
            }

        addVisitReviewDialog = AddVisitReviewDialog(this.requireContext())
        addVisitReviewDialog.createAddDeliveryReviewDialog { visitReview ->
            updateReviewList(
                visitReview
            )
        }

        addReviewButton.setOnClickListener {
            reviewTimeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            addVisitReviewDialog.resetDialog()
            addVisitReviewDialog.showPopup()
            addVisitReviewDialog.setReviewAddedTime(reviewTimeStamp)
            addVisitReviewDialog.reviewDialogRatingBar.rating = 0F
            updateAddVisitReviewDialogImages(
                true,
                addVisitReviewDialog.reviewDialogGalleryAdapter,
                addVisitReviewDialog.images,
                "",
                ModifyState.CLEAR
            )
        }
        setDialogAddPhoto(REQUEST_TAKE_PHOTO_ADD_REVIEW)
        setAddReviewDialogGalleryAdapter()


        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE
            )
        }
        reviewJsonResult(reviewReadJson())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.mapsView)

        if(mapView != null){
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST){
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                if (ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                googleMap.isMyLocationEnabled = true
            }
        }
        if (requestCode == REQUEST_TAKE_PHOTO_ADD_REVIEW || requestCode == REQUEST_TAKE_PHOTO_REVIEW) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent(requestCode)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(requestCode: Int): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File? =
            this.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var imageDir: File = File("")
        imageDir =
            if (requestCode == REQUEST_TAKE_PHOTO_ADD_REVIEW)
                File(storageDir, "/visit/$reviewTimeStamp")
            else
                File(storageDir, "/visit/" + visitReviewDialog.reviewTime)
        reviewImagesStorage = imageDir
        if (!imageDir.isDirectory) {
            imageDir.mkdirs()
        }
        return File.createTempFile(
            imageFileName,
            ".jpg",
            imageDir
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile(requestCode)
        } catch (ex: IOException) {
        }
        if (photoFile != null) run {
            val photoURI: Uri = FileProvider.getUriForFile(
                this.requireContext(),
                "com.example.myapplication.fileProvider",
                photoFile
            )
            takenPhoto = photoFile
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKE_PHOTO_ADD_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                updateAddVisitReviewDialogImages(
                    true,
                    addVisitReviewDialog.reviewDialogGalleryAdapter,
                    addVisitReviewDialog.images,
                    takenPhoto!!.absolutePath,
                    ModifyState.ADD
                )
            } else {
                takenPhoto?.delete()
            }
            addVisitReviewDialog.showPopup()
        } else if (requestCode == REQUEST_TAKE_PHOTO_REVIEW) {
            if (resultCode == Activity.RESULT_OK) {
                updateAddVisitReviewDialogImages(
                    false,
                    visitReviewDialog.reviewDialogGalleryAdapter,
                    visitReviewDialog.images,
                    takenPhoto!!.absolutePath,
                    ModifyState.ADD
                )
            } else {
                takenPhoto?.delete()
            }
            visitReviewDialog.popup()
            visitReviewDialog.editMode = true
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        if (p0 != null) {
            googleMap = p0
        }

        val mMap = googleMap
        var current_latitude: Double = 0.0
        var current_longitude: Double = 0.0
        val boundsBuilder = LatLngBounds.Builder()
        var check: Boolean = false

        val list = ArrayList<ContactData>()
        var i = 0

        val jsonArray = JSONArray(read_json())
        while(i < jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            val restaurant_type = jsonObject.getInt("restaurant_type")
            if(restaurant_type == 2){
                list.add(
                    ContactData(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        jsonObject.getString("food_type"),
                        jsonObject.getInt("restaurant_type"),
                        jsonObject.getString("phone_number"),
                        Place(
                            jsonObject.getJSONObject("location").getString("address"),
                            jsonObject.getJSONObject("location").getDouble("latitude"),
                            jsonObject.getJSONObject("location").getDouble("longitude")
                        )
                    )
                )
            }
            i++
        }

        for(contact in list){
            check = true
            val latLng = LatLng(contact.location.latitude, contact.location.longitude)
            boundsBuilder.include(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title(contact.name).snippet(contact.food_type))
        }

        if(ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        }
        else{
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
        }

        if(ActivityCompat.checkSelfPermission(this.requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location: Location? = it.result
                if (location != null) {
                    try {
                        val geocoder: Geocoder = Geocoder(this.requireContext(), Locale.getDefault())
                        val addresses = geocoder.getFromLocation(
                            location.latitude, location.longitude, 1
                        )
                        current_latitude = addresses.get(0).latitude
                        current_longitude = addresses.get(0).longitude

                        val current_latLng = LatLng(current_latitude, current_longitude)
//                        mMap.addMarker(
//                            MarkerOptions().position(current_latLng).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_latLng, 12.0f))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                else {
                    if(check)   mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
                    else    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 0.0)))
                }
            }
        }
        else{
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 44)
            if(check)   mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 0))
            else    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 0.0)))
        }
    }

    fun read_json(): String {
        var json: String = "[]"
        try {
            val inputStream = this.requireContext().openFileInput("contacts.json")
            json = inputStream.bufferedReader().use { it.readText() }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if(json == "")  return "[]"
        else    return json
    }

    private fun removeReview(visitReview: VisitReviewData) {
        val index = reviewListData.indexOf(visitReview)
        reviewListData.removeAt(index)
        val visitReviewAdapter = VisitReviewAdapter(this.requireContext(), reviewListData)
        reviewList.adapter = visitReviewAdapter
    }

    private fun reloadReviewList() {
        reviewJsonResult(reviewReadJson())
    }

    private fun reviewReadJson(): String {
        var json = "[]"
        try {
            val inputStream = this.requireContext().openFileInput("visit_review.json")
            json = inputStream.bufferedReader().use { it.readText() }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return if (json == "") "[]" else json
    }

    private fun reviewJsonResult(string: String) {
        val jsonArray = JSONArray(string)
        reviewListData = ArrayList<VisitReviewData>()
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            reviewListData.add(
                VisitReviewData(
                    jsonObject.getString("restaurant"),
                    jsonObject.getInt("rating"),
                    jsonObject.getString("review"),
                    jsonObject.getString("timeStamp")
                )
            )
        }
        val visitReviewAdapter = VisitReviewAdapter(this.requireContext(), reviewListData)
        reviewList.adapter = visitReviewAdapter
    }

    private fun setVisitReviewDialogGalleryAdapter() {
        visitReviewDialog.reviewDialogGalleryAdapter = GalleryAdapter(this.requireContext(),
            visitReviewDialog.images,
            object : GalleryAdapter.PhotoListener {
                override fun onPhotoClick(path: String) {
                    val editMode = visitReviewDialog.editMode
                    if (editMode) {
                        updateAddVisitReviewDialogImages(
                            true,
                            visitReviewDialog.reviewDialogGalleryAdapter,
                            visitReviewDialog.images,
                            path,
                            ModifyState.DELETE
                        )
                        val file = File(path)
                        file.delete()

                    } else {
                        val intent = Intent(activity, ImageSlider::class.java).apply {
                            putExtra("path", path)
                            putExtra("time", visitReviewDialog.reviewTime)
                            putExtra("type", "visit")
                        }
                        startActivity(intent)
                    }
                }
            })
        visitReviewDialog.reviewDialogRecyclerView.adapter =
            visitReviewDialog.reviewDialogGalleryAdapter
    }

    private fun setAddReviewDialogGalleryAdapter() {
        addVisitReviewDialog.reviewDialogGalleryAdapter =
            GalleryAdapter(this.requireContext(),
                addVisitReviewDialog.images,
                object : GalleryAdapter.PhotoListener {
                    override fun onPhotoClick(path: String) {
                        val editMode = addVisitReviewDialog.editMode
                        if (editMode) {
                            updateAddVisitReviewDialogImages(
                                true,
                                addVisitReviewDialog.reviewDialogGalleryAdapter,
                                addVisitReviewDialog.images,
                                path,
                                ModifyState.DELETE
                            )
                            val file = File(path)
                            file.delete()

                        } else {
                            val intent = Intent(activity, ImageSlider::class.java).apply {
                                putExtra("path", path)
                                putExtra("time", addVisitReviewDialog.timeStampReviewAdded)
                                putExtra("type", "visit")
                            }
                            startActivity(intent)
                        }
                    }
                })
        addVisitReviewDialog.reviewDialogRecyclerView.adapter =
            addVisitReviewDialog.reviewDialogGalleryAdapter
    }

    private fun updateAddVisitReviewDialogImages(
        addVisit: Boolean,
        galleryAdapter: GalleryAdapter,
        images: ArrayList<String>,
        image: String,
        modifyState: ModifyState,
    ) {
        when (modifyState) {
            ModifyState.ADD -> run {
                val len = images.size
                images.add(len, image)
                galleryAdapter.notifyItemInserted(len)
                if (addVisit)
                    setAddReviewDialogGalleryAdapter()
                else
                    setVisitReviewDialogGalleryAdapter()
            }
            ModifyState.DELETE -> {
                val index = images.indexOf(image)
                images.removeAt(index)
                galleryAdapter.notifyItemRemoved(index)
            }
            ModifyState.CLEAR -> {
                images.clear()
                galleryAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateReviewList(visitReview: VisitReviewData) {
        reviewListData.add(visitReview)
        val visitReviewAdapter = VisitReviewAdapter(this.requireContext(), reviewListData)
        reviewList.adapter = visitReviewAdapter
    }

    private fun setDialogAddPhoto(requestCode: Int) {
        if (requestCode == REQUEST_TAKE_PHOTO_ADD_REVIEW) {
            addVisitReviewDialog.reviewDialogAddPhoto.isClickable = true
            addVisitReviewDialog.reviewDialogAddPhoto.setOnClickListener {
                if (addVisitReviewDialog.editMode) {
                    addVisitReviewDialog.editMode = false
                    addVisitReviewDialog.editButton.visibility = View.VISIBLE
                    addVisitReviewDialog.reviewDialogAddPhoto.setImageResource(R.drawable.add_photo)

                } else {
                    if (ContextCompat.checkSelfPermission(
                            this.requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                            this.requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        dispatchTakePictureIntent(requestCode)
                        addVisitReviewDialog.dismissPopup()
                    } else {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            requestCode
                        )
                    }
                }
            }
        } else {
            visitReviewDialog.review_dialog_add_photo.isClickable = true
            visitReviewDialog.review_dialog_add_photo.setOnClickListener {
                if (visitReviewDialog.editMode) {
                    visitReviewDialog.editMode = false
                    visitReviewDialog.editButton.visibility = View.VISIBLE
                    visitReviewDialog.review_dialog_add_photo.setImageResource(R.drawable.add_photo)
                } else {
                    if (ContextCompat.checkSelfPermission(
                            this.requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(
                            this.requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        dispatchTakePictureIntent(requestCode)
                        visitReviewDialog.dismiss()
                    } else {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            requestCode
                        )
                    }

                }
            }
        }

    }
}