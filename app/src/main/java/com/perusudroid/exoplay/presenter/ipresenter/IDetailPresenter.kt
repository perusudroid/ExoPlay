package com.perusudroid.exoplay.presenter.ipresenter

interface IDetailPresenter : IPresenter {
    fun addOrUpdateVidInfo(currentPosition: Long, state: Int)
    fun changeVideo()
    fun isPlayableVideo(): Boolean
    fun getPlayablePosition(): Long
}