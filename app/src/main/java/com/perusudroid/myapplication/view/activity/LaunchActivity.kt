package com.perusudroid.myapplication.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.perusudroid.myapplication.R
import com.perusudroid.myapplication.adapter.LaunchAdapter
import com.perusudroid.myapplication.adapter.listener.IBaseListener
import com.perusudroid.myapplication.model.VideoListResponse
import com.perusudroid.myapplication.presenter.LaunchPresenter
import com.perusudroid.myapplication.presenter.ipresenter.ILaunchPresenter
import com.perusudroid.myapplication.view.iview.ILaunchView
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : BaseActivity(), ILaunchView, IBaseListener {


    private lateinit var iLaunchPresenter: ILaunchPresenter


    override fun getLayoutResId(): Int = R.layout.activity_launch

    override fun postInit(extras: Bundle?) {
        Log.d(TAG, "postInit")
        iLaunchPresenter = LaunchPresenter(this)
        iLaunchPresenter.onCreatePresenter(intent.extras)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun setRecyclerAdapter(vidList: List<VideoListResponse>) {
        recyclerView.adapter = LaunchAdapter(vidList, this)
    }

    override fun setEmptyView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onClick(data: Any?, position: Int) {

    }
}
