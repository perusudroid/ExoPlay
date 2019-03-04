package com.perusudroid.myapplication.presenter.ipresenter

import com.perusudroid.myapplication.presenter.BasePresenter

interface IDetailPresenter : IPresenter {
    fun addOrUpdateVidInfo( currentPosition: Long, state: Int)
    fun changeVideo()
    fun isPlayableVideo(): Boolean
    fun getPlayablePosition(): Long
}