package com.perusudroid.exoplay.presenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.perusudroid.exoplay.model.VideoListResponse
import com.perusudroid.exoplay.presenter.ipresenter.ILaunchPresenter
import com.perusudroid.exoplay.retrofit.ApiClient
import com.perusudroid.exoplay.view.iview.ILaunchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaunchPresenter(val iLaunchView: ILaunchView) : BasePresenter(iLaunchView), ILaunchPresenter,Callback<List<VideoListResponse>> {



    override fun onCreatePresenter(bundle: Bundle?) {
        super.onCreatePresenter(bundle)
        Log.d(TAG,"onCreatePresenter")
        if(iLaunchView.hasNetworkConnection()){
            doFetchApiData()
        }else{
            iLaunchView.showMessage("No internet connection found!")
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