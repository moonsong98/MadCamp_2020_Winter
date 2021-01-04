package com.example.myapplication

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONArray
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class VisitReview : Fragment(), OnMapReadyCallback {
    lateinit var googleMap: GoogleMap
    lateinit var mapView: MapView
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

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

        if(ActivityCompat.checkSelfPermission(this.requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                val location: Location = it.result
                if (location != null) {
                    try {
                        val geocoder: Geocoder = Geocoder(this.requireContext(), Locale.getDefault())
                        val addresses = geocoder.getFromLocation(
                            location.latitude, location.longitude, 1
                        )
                        current_latitude = addresses.get(0).latitude
                        current_longitude = addresses.get(0).longitude

                        val current_latLng = LatLng(current_latitude, current_longitude)
                        mMap.addMarker(
                            MarkerOptions().position(current_latLng).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        )
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
}