package com.perusudroid.myapplication.presenter.ipresenter

import android.content.Intent
import android.os.Bundle



interface IPresenter {


    fun onCreatePresenter(bundle: Bundle?)

    fun onStartPresenter()

    fun onStopPresenter()

    fun onPausePresenter()

    fun onResumePresenter()

    fun onDestroyPresenter()

    fun onActivityResultPresenter(requestCode: Int, resultCode: Int, data: Intent)

}