package com.perusudroid.exoplay.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.perusudroid.exoplay.model.BundlePOJO
import com.perusudroid.exoplay.model.VideoListResponse
import com.perusudroid.exoplay.presenter.ipresenter.IDetailPresenter
import com.perusudroid.exoplay.sqlite.DBHelper
import com.perusudroid.exoplay.view.iview.IDetailView

class DetailPresenter(val iDetailView: IDetailView) : BasePresenter(iDetailView), IDetailPresenter {


    private var vidList: List<VideoListResponse>? = null
    private var playWhenReady: Boolean = false
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var playablePosition = 1


    override fun onCreatePresenter(bundle: Bundle?) {
        super.onCreatePresenter(bundle)
        if (bundle != null) {

            var tmp: BundlePOJO

            with(bundle) {
                tmp = (getSerializable(com.perusudroid.exoplay.common.Constants.BundleKeys.DATA) as BundlePOJO)
                vidList = tmp.mList
                playablePosition = tmp.playablePosition
                playWhenReady = getBoolean(com.perusudroid.exoplay.common.Constants.BundleKeys.PLAY_WHEN_READY)
                currentWindow = getInt(com.perusudroid.exoplay.common.Constants.BundleKeys.KEY_WINDOW)
                playbackPosition = getLong(com.perusudroid.exoplay.common.Constants.BundleKeys.KEY_POSITION)
            }

            Log.d(TAG, "playablePosition $playablePosition vidList size ${vidList?.size}")

            iDetailView.setBundleStuffs(playWhenReady, playbackPosition, currentWindow, Uri.parse(vidList!![playablePosition].url), tmp)
        }
    }

    /**
     * Loop to the next video
     */

    override fun changeVideo() {


        val orgSize = vidList?.size!! + 1

        Log.d("Detail","playablePosition $playablePosition vidList ${vidList?.size} org size $orgSize")


        if (playablePosition == (vidList?.size!!-1)) {
            iDetailView.showMessage("You have reached end of the list")
        }else{
            iDetailView.showMessage("Playing next video")
            val tmp = vidList!![playablePosition]
            playablePosition++
            iDetailView.onVideoChanged(tmp)
        }


    }

    /**
     * Update or add video item info to local DB
     */

    override fun addOrUpdateVidInfo(currentPosition: Long, state: Int) {



        DBHelper.getInstance(iDetailView.getActivity() as Context).addOrUpdateOnConflict(vidList!![playablePosition].id!!, currentPosition, state)

    }

    override fun isPlayableVideo(): Boolean {

        if (playablePosition == vidList?.size) {
            playablePosition = vidList!!.size - 1
        }


        return DBHelper.getInstance(iDetailView.getActivity()).isPlayableVidExists(vidList!![playablePosition].id!!)
    }

    override fun getPlayablePosition(): Long {
        return DBHelper.getInstance(iDetailView.getActivity()).getVidPosition(vidList!![playablePosition].id!!)
    }


    override fun onActivityResultPresenter(requestCode: Int, resultCode: Int, data: Intent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}