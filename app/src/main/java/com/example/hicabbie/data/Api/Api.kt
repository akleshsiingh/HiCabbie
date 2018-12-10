package com.example.hicabbie.data.Api

import com.example.hicabbie.data.Constants
import com.example.hicabbie.data.response.ResponseLocation
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET(Constants.EXPLORE)
    fun getLocation(): Single<ResponseLocation>
}