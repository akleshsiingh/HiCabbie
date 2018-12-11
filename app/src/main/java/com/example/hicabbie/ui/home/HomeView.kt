package com.example.hicabbie.ui.home

import com.example.hicabbie.data.response.ResponseLocation

interface HomeView {


    fun onError(msg: String)
     fun updateButtonStatus(started: Boolean)
     fun updateLocation(loc: ResponseLocation)
}