package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray

class VisitReview : Fragment(), OnMapReadyCallback {
    lateinit var googleMap: GoogleMap
    lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_visitreview, container, false)
//        val intent = Intent(this.requireContext(), DisplayMap::class.java)

//        intent.putExtra(EXTRA_USER_MAP, list)
//
//        startActivity(intent)

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

        val boundsBuilder = LatLngBounds.Builder()

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
            val latLng = LatLng(contact.location.latitude, contact.location.longitude)
            boundsBuilder.include(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title(contact.name).snippet(contact.food_type))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 1000, 1000, 100))
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