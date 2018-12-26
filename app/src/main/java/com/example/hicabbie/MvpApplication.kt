package com.example.hicabbie

import android.app.Application
import android.arch.lifecycle.ProcessLifecycleOwner
import com.example.di.DaggerAppComponent
import com.example.hicabbie.utils.LifeCycleManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MvpApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerAppComponent.builder().bindApplication(this).build()
        component.inject(this)
        return component
    }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(LifeCycleManager(this))
    }

}