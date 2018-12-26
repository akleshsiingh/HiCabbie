package com.example.hicabbie.service

import android.app.Service
import android.content.Context
import android.content.Intent
import com.example.hicabbie.R

import com.example.hicabbie.data.db.DaoMessage
import com.example.hicabbie.data.db.entity.EMessage
import com.example.hicabbie.utils.Logger
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MessageService : Service() {
    override fun onBind(intent: Intent?) = null

    @Inject
    lateinit var mDao: DaoMessage

    private val TAG: String = MessageService::class.java.simpleName
    private val cd = CompositeDisposable()
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        Logger.e(TAG, " hash -- ${System.identityHashCode(mDao)}")
        startSendingFAkeMessages()
    }

    private fun startSendingFAkeMessages() {
        cd.add(
            Observable.interval(5, TimeUnit.SECONDS)
                .startWith(3)
                //   .filter { it % 3 == 0L }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    val msg = EMessage()
                    if (Random().nextBoolean()) {
                        msg.name = "user 1 mine"
                        msg.mine = true
                        msg.msg = getString(R.string.first_user_msg)
                    } else {
                        msg.name = "Other"
                        msg.mine = false
                        msg.msg = getString(R.string.other_user_msg)
                    }
                    val id = mDao.insertMessage(msg)
                    Logger.e(TAG, " interval $it inserted at $id")
                }, {
                    Logger.e(TAG, " error ${it.localizedMessage}")
                })
        )

    }

    companion object {
        fun getNewIntent(context: Context) = Intent(context, MessageService::class.java)
    }

    override fun onDestroy() {
        cd.dispose()
        super.onDestroy()
    }
}

