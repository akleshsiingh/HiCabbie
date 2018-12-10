package com.example.hicabbie.ui.home

import com.example.hicabbie.data.response.ResponseLocation
import io.reactivex.Single

interface ILocationRepo {

    fun getLocation():Single<ResponseLocation>
}