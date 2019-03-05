package com.perusudroid.exoplay.view.activity

import android.content.Intent
import android.os.Bundle
import com.perusudroid.exoplay.R
import com.perusudroid.exoplay.presenter.LoginPresenter
import com.perusudroid.exoplay.presenter.ipresenter.ILoginPresenter
import com.perusudroid.exoplay.view.iview.ILoginView
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() , ILoginView{


    private lateinit var iLoginPresenter: ILoginPresenter

    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun postInit(extras: Bundle?) {

        if(supportActionBar != null){
            supportActionBar?.title = "Login"
        }

        iLoginPresenter = LoginPresenter(this)
        iLoginPresenter.onCreatePresenter(extras)

        login_with_google.setOnClickListener {

            iLoginPresenter.doLogin()

        }

    }

    override fun startLaunchActivity() {
        val intent = Intent(this@LoginActivity, LaunchActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        iLoginPresenter.onActivityResultPresenter(requestCode, resultCode, data!!)
    }



}
