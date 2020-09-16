package com.example.whereyouat.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whereyouat.R
import com.example.whereyouat.viewmodel.MyViewModel
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    /**Variables needed on a global scope*/
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient:FusedLocationProviderClient
    private lateinit var locationRequest:LocationRequest
    private lateinit var locationCallback:LocationCallback
    private lateinit var viewModel: MyViewModel
    private lateinit var userName:String
    private lateinit var friendName:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        /**Obtain the SupportMapFragment and get notified when the map is ready to be used.*/

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        /**Retrive arguments passed*/
        userName = intent.getStringExtra("USERNAME").toString()
        friendName = intent.getStringExtra("FRIEND_NAME").toString()
        

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        /**Make custom marker*/
        val bitmaps: Bitmap? = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.victor_marker), 150, 150, true)



        /**Observe changes in location of friend and move the position of the marker accordingly*/
        viewModel.friend.observe(this, Observer{
            map.clear()
            Log.d("Observing","${it.longitude} ${it.latitude}")
            val longitude = it.longitude
            val latitude = it.latitude
            val name = it.name

            val location = LatLng(latitude,longitude)
            map.addMarker(MarkerOptions().position(location).title(name))
                .setIcon(BitmapDescriptorFactory.fromBitmap(bitmaps))
            //map.moveCamera(CameraUpdateFactory.newLatLng(location))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        })

        /**On map ready find the friend's location*/
        viewModel.findFriendLocation(friendName)

        /**Subscribe for location updates*/
        getUpDates()


    }

    private fun getUpDates(){
        /**Request location updates from the android system*/
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 30000
        locationRequest.fastestInterval = 30000
       // locationRequest.smallestDisplacement
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    viewModel.updateLocation(location,userName)

                }
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}