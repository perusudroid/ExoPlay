package com.perusudroid.myapplication.presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.perusudroid.myapplication.model.VideoListResponse
import com.perusudroid.myapplication.presenter.ipresenter.ILaunchPresenter
import com.perusudroid.myapplication.retrofit.ApiClient
import com.perusudroid.myapplication.view.iview.ILaunchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaunchPresenter(val iLaunchView: ILaunchView) : BasePresenter(iLaunchView), ILaunchPresenter,Callback<List<VideoListResponse>> {



    override fun onCreatePresenter(bundle: Bundle?) {
        super.onCreatePresenter(bundle)
        Log.d(TAG,"onCreatePresenter")
        if(iLaunchView.hasNetworkConnection()){
            doFetchApiData()
        }
    }

    private fun doFetchApiData() {
        Log.d(TAG,"doFetchApiData")
        ApiClient.apiInterface.getVidList().enqueue(this)
    }

    override fun onResponse(call: Call<List<VideoListResponse>>, response: Response<List<VideoListResponse>>) {
        if (response.code() == 200 && response.isSuccessful && response.body() != null) {
            iLaunchView.setRecyclerAdapter(response.body()!!)
        }
    }

    override fun onFailure(call: Call<List<VideoListResponse>>, t: Throwable) {
       iLaunchView.setEmptyView()
    }


    override fun onActivityResultPresenter(requestCode: Int, resultCode: Int, data: Intent) {

    }
}