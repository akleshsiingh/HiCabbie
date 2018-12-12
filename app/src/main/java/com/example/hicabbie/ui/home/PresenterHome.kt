package com.example.hicabbie.ui.home

import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.di.ActivityScoped
import com.example.hicabbie.data.response.ResponseLocation
import com.example.hicabbie.service.ServiceCar
import javax.inject.Inject

@ActivityScoped
class PresenterHome @Inject constructor(private val repo: ILocationRepo, private val view: HomeView) {
    lateinit var service: ServiceCar
    fun service(service: ServiceCar) {
        this.service = service
        view.updateButtonStatus(service.started)
        service.dismissNotification()
        service.updateListner(object : OnUpdateListner {
            override fun update(it: ResponseLocation) {
                view.updateLocation(it)
            }
        })
    }

    fun startPolling() {
        service.started = !service.started
        service.fetch()
        view.updateButtonStatus(service.started)
    }

    fun onStop(context: Context) {
        if (service.started) {
            service.showNotification()
            service.updateListner(null)
        } else
            ServiceCar.getNewIntent(context).also { context.applicationContext.stopService(it) }
    }

    fun checkLastLocation(): ResponseLocation? {
        return service.getLastLocation()?.apply { view.updateLocation(this) }
    }

    fun destroy() {

    }

}