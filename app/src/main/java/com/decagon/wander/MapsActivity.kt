package com.decagon.wander

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/*
* The map activity
* */

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    // Declare googleMap
    private lateinit var map: GoogleMap

    // Holding reference to this  mapactivity
    private val TAG = MapsActivity::class.java.simpleName

    // The request code
    private val REQUEST_LOCATION_PERMISSION = 1

    // fusedLocationClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Declaring location request
    private lateinit var locationRequest: LocationRequest

    // Locaiton call back
    private lateinit var locationCallback: LocationCallback

    // Database ref
    private lateinit var databaseRef: DatabaseReference

    //  onCreate callback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Using location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Database instance
        databaseRef = Firebase.database.reference
        // ValueEventListener for update
        databaseRef.addValueEventListener(logListener)
    }

    // ValuEventListener interface
    private val logListener = object : ValueEventListener {
        // onCancelled overidden
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(applicationContext, "Could not read from database", Toast.LENGTH_LONG)
                .show()
        }

        // onDataChange
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            val bitmaps: Bitmap? = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.david_marker), 200, 200, true)




            // Check if datasnapshot exits
            if (dataSnapshot.exists()) {
                // Get data from database
                val userLocation = dataSnapshot.child("David").getValue(UserLocation::class.java)
                var friendLat = userLocation?.latitude
                var friendLong = userLocation?.longitude

                // Confirm friend properties are not null
                if (friendLat != null && friendLong != null) {
                    val friendLoc = LatLng(friendLat, friendLong)
                    // Clear all existing map
                    map.clear()
                    // Add markers
                    val markerOptions =
                        MarkerOptions().position(friendLoc).title(userLocation!!.name)
                    map.addMarker(markerOptions)
                        .setIcon(BitmapDescriptorFactory.fromBitmap(bitmaps))

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLoc, 15f))

                }
            }
        }
    }

    /*
    * getLocaionUpdate method */
    private fun getLocationUpdates() {
        // LocationRequest instance
        locationRequest = LocationRequest()
        // Setting interval
        locationRequest.interval = 30000
        // Setting fastestInterval
        locationRequest.fastestInterval = 30000
        // Setting locationRequest Priority
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        // Overriding LocationCallback interface
        locationCallback = object : LocationCallback() {
            // Get last location through onLocationResult callback
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    //Database instance
                    lateinit var databaseRef: DatabaseReference
                    databaseRef = Firebase.database.reference
                    val userLocation = UserLocation("Victor", location.latitude, location.longitude)
                    //destruction
                    val (name, latitude, longitude) = userLocation

                    // Getting my Location
                    val myLoc = LatLng(latitude!!, longitude!!)
                    //Add marker
                    val markerOptions = MarkerOptions().position(myLoc).title(name)
                    map.addMarker(markerOptions)

                    // Persisting to Database
                    databaseRef.child(name!!).setValue(userLocation)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                "Locations written into the database",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Error occured while writing the locations",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)

//        val latitude = 6.473496
//        val longitude = 3.631116
//        val zoomLevel = 18f
//        val overlaySize = 100f
//
//
//        val homeLatLng = LatLng(latitude, longitude)


//        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
//        map.addMarker(MarkerOptions().position(homeLatLng))
//
//        val androidOverlay = GroundOverlayOptions()
//            .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
//            .position(homeLatLng, overlaySize)
//
//        map.addGroundOverlay(androidOverlay)

//        setMapLongClick(map)
//        setPoiClick(map)
        setMapStyle(map)

        enableMyLocation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun setMapStyle(map: GoogleMap) {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.setMyLocationEnabled(true)
            getLocationUpdates()
            startLocationUpdates()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            } else {
                Toast.makeText(
                    this,
                    "User has not granted location access permission",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
}