package com.example.whereyouat.viewmodel

import android.icu.util.Freezable
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whereyouat.model.Friend
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*

class MyViewModel:ViewModel() {

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var _friend = MutableLiveData<Friend>()
    val friend:LiveData<Friend>
        get() = _friend

    private var _found = MutableLiveData<Boolean>()
    val found:LiveData<Boolean>
    get() = _found







    var longLat:LatLng? = null

    fun updateLocation(location:Location, name:String) {
        val friend = Friend(name,location.latitude,location.longitude)
        Log.d("NewTest","${location.latitude},${location.longitude}")
        friend.name?.let { database.child(it).setValue(friend) }
    }


    init {


//        database.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapShot: DataSnapshot) {
//                val friendsData = snapShot.child("Victor").getValue(Friend::class.java)
//                Log.d("Friend","${friendsData?.longitude}")
//                _friend.value = friendsData
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
    }

    fun findUser(name:String){

        database.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("Friend","Block Executed")
                val data = p0.child("$name").value
                Log.d("Friend",data.toString())
               if (data.toString()== "null"){
                   _found.value = false

                   Log.d("Friend","Inner Block Executed")
               }else _found.value = true
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        } )
    }

    fun AddUser(friend:Friend){
        friend.name?.let { database.child(it).setValue(friend) }
    }




    fun logOUt(){
        _found.value =false
    }




    fun findFriendLocation(name:String){

        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapShot: DataSnapshot) {
                if (snapShot.child(name).value != null){
                    val friendsData = snapShot.child(name).getValue(Friend::class.java)
                    Log.d("Friend","${friendsData?.longitude}")
                    _friend.value = friendsData
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}