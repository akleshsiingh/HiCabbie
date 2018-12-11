package com.example.hicabbie.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.hicabbie.R
import com.example.hicabbie.data.response.ResponseLocation
import com.example.hicabbie.service.ServiceCar
import com.example.hicabbie.ui.base.BaseActivity
import com.example.hicabbie.utils.click
import kotlinx.android.synthetic.main.activity_main.*
import com.example.hicabbie.service.ServiceCar.MyBinder
import android.os.IBinder
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import javax.inject.Inject


class MainActivity : BaseActivity(), HomeView {

    @Inject
    lateinit var presenter: PresenterHome

    override fun onError(msg: String) {
    }

    override fun updateButtonStatus(started: Boolean) {
        if (started)
            ivBtnStart.setImageResource(R.drawable.ic_stop_24)
        else
            ivBtnStart.setImageResource(R.drawable.ic_start_24)
    }

    override fun updateLocation(loc: ResponseLocation) {

    }


    override fun gtContentView() = R.layout.activity_main

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        //to start
        ivBtnStart.click { presenter.startPolling() }
    }

    override fun onStart() {
        super.onStart()
        ServiceCar.getNewIntent(this).also {
            startService(it)
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bounded) {
            unbindService(connection)
            bounded = false
        }
        presenter.onStop(this)
    }

    private var bounded = false
    private val connection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            bounded = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBinder
            bounded = true
            presenter.service(myBinder.getService())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

interface OnUpdateListner {
    fun update(it: ResponseLocation)
}


