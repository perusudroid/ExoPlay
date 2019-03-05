package com.perusudroid.exoplay.view.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.perusudroid.exoplay.presenter.ipresenter.IPresenter
import com.perusudroid.exoplay.view.iview.IView

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

    override fun showMessage(txt: String) {
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show()
    }

    override fun bindPresenter(iPresenter: IPresenter) {
        this.iPresenter = iPresenter
    }

    override fun hasNetworkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}