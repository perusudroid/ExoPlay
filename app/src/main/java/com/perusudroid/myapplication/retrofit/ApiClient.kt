package com.perusudroid.myapplication.retrofit

import com.perusudroid.myapplication.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {

    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val apiInterface: ApiInterface

    init {
        apiInterface = retrofit.create(ApiInterface::class.java)
    }
}
