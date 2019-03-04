package com.perusudroid.myapplication.view.iview

import com.perusudroid.myapplication.model.VideoListResponse

interface ILaunchView : IView {

    fun setRecyclerAdapter(vidList: List<VideoListResponse>)
    fun setEmptyView()


}