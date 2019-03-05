package com.perusudroid.exoplay.view.iview

import com.perusudroid.exoplay.model.VideoListResponse

interface ILaunchView : IView {

    fun setRecyclerAdapter(vidLists: List<VideoListResponse>)
    fun setEmptyView()


}