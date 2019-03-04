package com.perusudroid.myapplication.view.iview

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.perusudroid.myapplication.presenter.ipresenter.IPresenter


interface IView {


    fun postInit(extras: Bundle?)

    fun hasNetworkConnection() : Boolean

    fun getLayoutResId(): Int

    fun getActivity(): FragmentActivity

    fun bindPresenter(iPresenter: IPresenter)

}