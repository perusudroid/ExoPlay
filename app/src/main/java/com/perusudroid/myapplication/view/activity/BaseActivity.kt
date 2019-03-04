package com.perusudroid.myapplication.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.perusudroid.myapplication.presenter.ipresenter.IPresenter
import com.perusudroid.myapplication.view.iview.IView

abstract class BaseActivity : AppCompatActivity() , IView{

    private var iPresenter : IPresenter? = null
    open val TAG : String = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        postInit(intent.extras)
    }


    override fun onStart() {
        super.onStart()
        if(iPresenter != null) iPresenter?.onStartPresenter()
    }


    override fun onResume() {
        super.onResume()
        if(iPresenter != null) iPresenter?.onResumePresenter()
    }


    override fun onPause() {
        super.onPause()
        if(iPresenter != null) iPresenter?.onPausePresenter()
    }

    override fun onStop() {
        super.onStop()
        if(iPresenter != null) iPresenter?.onStopPresenter()
    }


    override fun onDestroy() {
        super.onDestroy()
        if(iPresenter != null) iPresenter?.onDestroyPresenter()
    }

    override fun getActivity(): FragmentActivity = this

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun bindPresenter(iPresenter: IPresenter) {
        this.iPresenter = iPresenter
    }

    override fun hasNetworkConnection(): Boolean {
        return true
    }
}