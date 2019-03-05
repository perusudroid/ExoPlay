package com.perusudroid.exoplay.view.iview

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.perusudroid.exoplay.presenter.ipresenter.IPresenter


interface IView {


    fun postInit(extras: Bundle?)

    fun hasNetworkConnection() : Boolean


    fun showMessage(txt : String)

    fun getLayoutResId(): Int

    fun getActivity(): FragmentActivity

    fun bindPresenter(iPresenter: IPresenter)

}