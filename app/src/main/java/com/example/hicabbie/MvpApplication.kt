package com.example.hicabbie

import android.app.Application
import com.example.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MvpApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerAppComponent.builder().bindApplication(this).build()

        component.inject(this)
        return component
    }

}