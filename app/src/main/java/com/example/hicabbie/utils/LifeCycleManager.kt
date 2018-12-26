package com.example.hicabbie.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.example.hicabbie.service.MessageService

class LifeCycleManager(private val context: Context) : LifecycleObserver {

    private val TAG: String = LifeCycleManager::class.java.simpleName

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Logger.e(TAG," onStart")
        MessageService.getNewIntent(context)
            .also { context.startService(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Logger.e(TAG," ON_STOP")
        MessageService.getNewIntent(context)
            .also { context.stopService(it) }
    }
}