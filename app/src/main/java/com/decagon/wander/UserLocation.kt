package com.decagon.wander

import com.google.firebase.database.Exclude

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class UserLocation(
//    @get:Exclude
//    var id: String? = null, // The string is very important
    var name: String? = null,
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
)