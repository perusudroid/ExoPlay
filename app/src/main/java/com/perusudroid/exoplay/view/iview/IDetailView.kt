package com.perusudroid.exoplay.view.iview

import android.net.Uri
import com.perusudroid.exoplay.model.BundlePOJO
import com.perusudroid.exoplay.model.VideoListResponse

interface IDetailView : IView {


    fun setBundleStuffs(playWhenReady: Boolean, playbackPosition: Long, currentWindow: Int, currentUri: Uri, bundlePOJO: BundlePOJO)
    fun onVideoChanged(vidList: VideoListResponse)


}