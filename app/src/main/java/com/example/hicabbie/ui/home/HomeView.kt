package com.example.hicabbie.ui.home

import com.example.hicabbie.data.response.ResponseLocation

interface HomeView {

    fun onUpdate(resoponse: ResponseLocation)

    fun onError(msg: String)
}