package com.perusudroid.exoplay.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.perusudroid.exoplay.R
import com.perusudroid.exoplay.adapter.LaunchAdapter
import com.perusudroid.exoplay.adapter.listener.IBaseListener
import com.perusudroid.exoplay.common.Constants
import com.perusudroid.exoplay.model.BundlePOJO
import com.perusudroid.exoplay.model.VideoListResponse
import com.perusudroid.exoplay.presenter.LaunchPresenter
import com.perusudroid.exoplay.presenter.ipresenter.ILaunchPresenter
import com.perusudroid.exoplay.view.iview.ILaunchView
import kotlinx.android.synthetic.main.activity_launch.*

class LaunchActivity : BaseActivity(), ILaunchView, IBaseListener {


    private lateinit var iLaunchPresenter: ILaunchPresenter
    private lateinit var vidList: MutableList<VideoListResponse>


    override fun getLayoutResId(): Int = R.layout.activity_launch

    override fun postInit(extras: Bundle?) {
        Log.d(TAG, "postInit")
        if (supportActionBar != null) {
            supportActionBar?.title = "Home"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        iLaunchPresenter = LaunchPresenter(this)
        iLaunchPresenter.onCreatePresenter(intent.extras)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun setRecyclerAdapter(vidLists: List<VideoListResponse>) {
        this.vidList = vidLists as MutableList<VideoListResponse>
        recyclerView.adapter = LaunchAdapter(vidList, this)

        vsParent.setChildVisible()
    }

    override fun setEmptyView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(data: Any?, position: Int) {


        val intent = Intent(this@LaunchActivity, DetailActivity::class.java)

        val bundlePOJO = BundlePOJO(vidList, position,vidList[position].title!!, vidList[position].description!!)
        intent.putExtra(Constants.BundleKeys.DATA, bundlePOJO)
        startActivity(intent)
    }
}
