package com.perusudroid.myapplication.retrofit

import com.perusudroid.myapplication.model.VideoListResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/media.json?print=pretty")
    fun getVidList(): Call<List<VideoListResponse>>


}