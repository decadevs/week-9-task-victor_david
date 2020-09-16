package com.example.whereyouat

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.whereyouat.model.Friend
import com.example.whereyouat.ui.FindFriendFragment
import com.example.whereyouat.ui.SignUpFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {


    /**Variable needed on a global scope*/
    var loggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences("USER", Context.MODE_PRIVATE)

        /**Saved preference to know if user is logged in*/
         loggedIn = sharedPref.getBoolean("LoggedIn",false)

        val hasPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

        /**Request location permission is not granted*/
        if (!hasPermission){
            askPermission()
        }else initUI()

    }


    /**Function to set up UI based on if the user was logged in using saved preferences */
    private fun initUI(){
        if(loggedIn){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fvFrame,FindFriendFragment())
                commit()
            }
        }else {
            supportFragmentManager.beginTransaction().apply {

                replace(R.id.fvFrame,SignUpFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    /**Fundtion to ask for permission*/
    private fun askPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),0)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUI()
            }else Toast.makeText(this,"Permission To Access Location Is Required",Toast.LENGTH_LONG).show()
        }
    }
}