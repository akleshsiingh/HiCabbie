package com.example.hicabbie.ui.home

import com.example.hicabbie.data.api.Api
import com.example.hicabbie.data.response.ResponseLocation
import io.reactivex.Single

class LocationRepoIMPL(private val api: Api, private val home: HomeView) : ILocationRepo {

    override fun getLocation(): Single<ResponseLocation> = api.getLocation()

}