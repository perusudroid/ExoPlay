package com.perusudroid.exoplay.retrofit

import com.perusudroid.exoplay.model.VideoListResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/media.json?print=pretty")
    fun getVidList(): Call<List<VideoListResponse>>


}