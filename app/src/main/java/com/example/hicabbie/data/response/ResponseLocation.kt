package com.example.hicabbie.data.response

import com.squareup.moshi.Json

data class ResponseLocation(

    @Json(name = "latitude")
    val latitude: Double = 0.0,

    @Json(name = "status")
    val status: String = "",

    @Json(name = "longitude")
    val longitude: Double = 0.0
)