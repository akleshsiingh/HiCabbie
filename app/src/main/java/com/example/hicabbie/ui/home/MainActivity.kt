package com.example.hicabbie.ui.home

import android.content.Intent
import android.os.Bundle
import com.example.hicabbie.R
import com.example.hicabbie.data.response.ResponseLocation
import com.example.hicabbie.service.ServiceCar
import com.example.hicabbie.ui.base.BaseActivity
import com.example.hicabbie.utils.click
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() ,HomeView{

    override fun onUpdate(resoponse: ResponseLocation) {
    }

    override fun onError(msg: String) {
    }

    override fun gtContentView() = R.layout.activity_main

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        btnStart.click {
            if (!ServiceCar.started)
                startService(ServiceCar.getNewIntent(this))
            else
                stopService(ServiceCar.getNewIntent(this))
            ServiceCar.started = !ServiceCar.started
        }
    }

    override fun onDestroy() {
        if (ServiceCar.started)
            stopService(ServiceCar.getNewIntent(this))
        super.onDestroy()
    }
}


