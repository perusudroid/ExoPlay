package com.perusudroid.myapplication.view.activity

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import com.perusudroid.myapplication.R
import com.perusudroid.myapplication.common.Constants
import com.perusudroid.myapplication.model.VideoListResponse
import com.perusudroid.myapplication.presenter.DetailPresenter
import com.perusudroid.myapplication.presenter.ipresenter.IDetailPresenter
import com.perusudroid.myapplication.view.iview.IDetailView

class DetailActivity : BaseActivity(), IDetailView {


    private var mRunnable: Runnable? = null
    private var mHandler: Handler? = null


    private var playWhenReady: Boolean = false
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var playablePosition = 1

    private var player: SimpleExoPlayer? = null
    private val playerView: PlayerView by lazy { findViewById<PlayerView>(R.id.player_view) }
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private lateinit var iDetailPresenter: IDetailPresenter

    private var shouldAutoPlay: Boolean = true
    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
    private var currentUri: Uri? = null


    override fun getLayoutResId(): Int = R.layout.activity_detail

    override fun postInit(extras: Bundle?) {


        shouldAutoPlay = false
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"),
                bandwidthMeter as TransferListener<in DataSource>)


        iDetailPresenter = DetailPresenter(this)
        iDetailPresenter.onCreatePresenter(extras)
        startProcessingStuffs()

    }

    override fun setBundleStuffs(playWhenReady: Boolean, playbackPosition: Long, currentWindow: Int, currentUri: Uri) {
        this.currentUri = currentUri
    }


    private fun startProcessingStuffs() {
        mHandler = Handler()

        mRunnable = Runnable {
            iDetailPresenter.addOrUpdateVidInfo(player!!.currentPosition, Constants.Common.STATE_PLAYING)
            resumeCallBack()
        }

        mHandler?.postDelayed(mRunnable, Constants.Common.HANDLER_MSS)
    }


    override fun setRecyclerAdapter(vidList: List<VideoListResponse>) {

    }

    override fun setEmptyView() {

    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) initializePlayer()
    }


    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        if (player != null) {
            updateStartPosition()
            shouldAutoPlay = player!!.playWhenReady
            player!!.release()
            player = null
            trackSelector = null
        }
    }

    private fun updateStartPosition() {

        with(player!!) {
            playbackPosition = currentPosition
            currentWindow = currentWindowIndex
            playWhenReady = playWhenReady
        }
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        removeCallBack()
        if (Util.SDK_INT <= 23) releasePlayer()

    }

    private fun removeCallBack() {

        if (mHandler != null && mRunnable != null) {
            mHandler?.removeCallbacks(mRunnable)
        }
    }

    private fun resumeCallBack() {
        if (mHandler != null) {
            mHandler?.postDelayed(mRunnable, 1000)
        } else {
            Log.e(TAG, "Error Handler is null")
        }
    }


    private fun initializePlayer() {

        playerView.requestFocus()

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        lastSeenTrackGroupArray = null

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, DefaultLoadControl())

        playerView.player = player

        with(player!!) {
            addListener(PlayerEventListener())
            playWhenReady = shouldAutoPlay
        }


        val haveStartPosition = currentWindow != C.INDEX_UNSET
        val isPlayable = iDetailPresenter.isPlayableVideo()


        Log.d(TAG, "isCrapPlayable $isPlayable")

        if (isPlayable) {
            //alive video
            val pos = iDetailPresenter.getPlayablePosition()
            shouldAutoPlay = true
            player!!.seekTo(currentWindow, pos)
            player!!.prepare(getMediaSource(), !haveStartPosition, false)
        } else {
            player!!.seekTo(currentWindow, playbackPosition)
            player!!.prepare(getMediaSource(), !haveStartPosition, false)
        }

    }


    private inner class PlayerEventListener : Player.DefaultEventListener() {

        override fun onLoadingChanged(isLoading: Boolean) {
            super.onLoadingChanged(isLoading)
            Log.d(TAG, "isLoading $isLoading")
        }

        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
            Log.d(TAG, "reason $reason")
        }


        override fun onSeekProcessed() {
            super.onSeekProcessed()
            Log.d(TAG, "Seeking")
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {


            if (playWhenReady && playbackState == Player.STATE_READY) {
                // media actually playing
                progressBar.visibility = View.GONE

                resumeCallBack()

            } else {

                removeCallBack()


                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.visibility = View.VISIBLE
                }

                if (playbackState == Player.STATE_IDLE) {
                    progressBar.visibility = View.INVISIBLE
                }

                iDetailPresenter.addOrUpdateVidInfo(player!!.currentPosition, Constants.Common.STATE_PLAYING)



                if (playbackState == Player.STATE_ENDED) {

                    iDetailPresenter.addOrUpdateVidInfo(player!!.currentPosition, Constants.Common.STATE_NOT_PLAYING)


                    progressBar.visibility = View.GONE

                    iDetailPresenter.changeVideo()

                    changeVideo()
                }

            }


        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            // The video tracks are no supported in this device.
            if (trackGroups !== lastSeenTrackGroupArray) {
                val mappedTrackInfo = trackSelector!!.currentMappedTrackInfo
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(this@DetailActivity, "Error unsupported track", Toast.LENGTH_SHORT).show()
                    }
                }
                lastSeenTrackGroupArray = trackGroups
            }
        }


        private fun changeVideo() {

        }
    }

    private fun getMediaSource(): MediaSource? = ExtractorMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(currentUri)


    override fun onVideoChanged(uri: Uri?) {
        currentUri = uri
        player?.stop()
        player?.seekTo(0L)
        //you must change your contentUri before invoke getRendererBuilder();
        player!!.prepare(getMediaSource(), false, false)
    }

}
