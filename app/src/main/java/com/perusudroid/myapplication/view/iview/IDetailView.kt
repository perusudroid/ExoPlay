package com.perusudroid.myapplication.view.iview

import android.net.Uri
import com.perusudroid.myapplication.model.VideoListResponse

interface IDetailView : IView {

    fun setRecyclerAdapter(vidList: List<VideoListResponse>)
    fun setEmptyView()
    fun setBundleStuffs(playWhenReady: Boolean, playbackPosition: Long, currentWindow: Int, currentUri: Uri)
    fun onVideoChanged(uri: Uri?)


}