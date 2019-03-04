package com.perusudroid.myapplication.presenter

import android.os.Bundle
import com.perusudroid.myapplication.presenter.ipresenter.IPresenter
import com.perusudroid.myapplication.view.iview.IView


abstract class BasePresenter(iview: IView) : IPresenter {

    private var iView: IView = iview

    open val TAG : String = javaClass.simpleName

    init {
        iView.bindPresenter(this)
    }



    override fun onCreatePresenter(bundle: Bundle?) {

    }

    override fun onStartPresenter() {
        
    }

    override fun onResumePresenter() {

    }

    override fun onPausePresenter() {

    }

    override fun onStopPresenter() {

    }

    override fun onDestroyPresenter() {

    }

}