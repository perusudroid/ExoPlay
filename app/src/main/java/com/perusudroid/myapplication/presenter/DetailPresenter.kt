package com.perusudroid.myapplication.presenter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.perusudroid.myapplication.model.BundlePOJO
import com.perusudroid.myapplication.model.VideoListResponse
import com.perusudroid.myapplication.presenter.ipresenter.IDetailPresenter
import com.perusudroid.myapplication.sqlite.DBHelper
import com.perusudroid.myapplication.view.iview.IDetailView

class DetailPresenter(val iDetailView: IDetailView) : BasePresenter(iDetailView), IDetailPresenter {


    private var vidList: List<VideoListResponse>? = null
    private var playWhenReady: Boolean = false
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var playablePosition = 1


    override fun onCreatePresenter(bundle: Bundle?) {
        super.onCreatePresenter(bundle)
        if (bundle != null) {

            with(bundle) {
                vidList = (getSerializable(com.perusudroid.myapplication.common.Constants.BundleKeys.DATA) as BundlePOJO).mList
                playWhenReady = getBoolean(com.perusudroid.myapplication.common.Constants.BundleKeys.PLAY_WHEN_READY)
                currentWindow = getInt(com.perusudroid.myapplication.common.Constants.BundleKeys.KEY_WINDOW)
                playbackPosition = getLong(com.perusudroid.myapplication.common.Constants.BundleKeys.KEY_POSITION)
                playablePosition = getInt(com.perusudroid.myapplication.common.Constants.BundleKeys.PLAYABLE_POSITION)
            }

            iDetailView.setBundleStuffs(playWhenReady,playbackPosition, currentWindow, Uri.parse(vidList!![playablePosition].url))
        }
    }

    override fun changeVideo() {
       iDetailView.onVideoChanged( Uri.parse(vidList!![playablePosition].url))
    }


    override fun addOrUpdateVidInfo(currentPosition: Long, state: Int) {

        DBHelper.getInstance(iDetailView.getActivity()).addOrUpdateOnConflict(vidList!![playablePosition].id!!, currentPosition, state)


    }


    override fun isPlayableVideo(): Boolean {
       return DBHelper.getInstance(iDetailView.getActivity()).isPlayableVidExists(vidList!![playablePosition].id!!)
    }

    override fun getPlayablePosition(): Long {
       return DBHelper.getInstance(iDetailView.getActivity()).getVidPosition(vidList!![playablePosition].id!!)
    }


    override fun onActivityResultPresenter(requestCode: Int, resultCode: Int, data: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}