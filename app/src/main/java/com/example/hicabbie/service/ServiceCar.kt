package com.example.hicabbie.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.example.hicabbie.R
import com.example.hicabbie.data.Api.Api
import com.example.hicabbie.data.response.ResponseLocation
import com.example.hicabbie.ui.home.OnUpdateListner
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ServiceCar : Service() {


    @Inject
    lateinit var api: Api
    private lateinit var notificationManager: NotificationManager
    private lateinit var context: Context
    private var cd = CompositeDisposable()
    private val iBinder: IBinder = MyBinder()
    var started = false
    private val NOTIF_ID = 1106

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        context = applicationContext
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val TAG = ServiceCar::class.java.simpleName
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    fun fetch() {
        if (cd.isDisposed)
            cd = CompositeDisposable()
        if (started)
            cd.add(Observable
                .interval(1, TimeUnit.SECONDS)
                .startWith(0)
                .flatMap { api.getLocation().toObservable() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateListner?.update(it)
                    updateNotification(it)
                }, { it.printStackTrace() })
            )
        else cd.dispose()

    }

    var updateListner: OnUpdateListner? = null
    fun updateListner(updateListner: OnUpdateListner?) {
        this.updateListner = updateListner
    }

    private fun updateNotification(loc: ResponseLocation) {
        remoteViews?.let {
            it.setTextViewText(R.id.tvProgress, "location updates in progress - ${loc.latitude} - ${loc.longitude} ")
            notificationManager.notify(NOTIF_ID, notification?.build())
        }
    }

    private var remoteViews: RemoteViews? = null
    private var notification: NotificationCompat.Builder? = null

    fun showNotification() {

        val channelID = "channelID"
        val channelName = "CHANNEL_NAME"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    description = "title"
                    enableLights(true)
                    lightColor = Color.RED
                }
            notificationManager.createNotificationChannel(channel)
        }
        remoteViews = RemoteViews(context.packageName, R.layout.notify_row)
//      Notification
        notification = NotificationCompat.Builder(context, channelID)
            .apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setOngoing(true)
                setTicker("title")
                setDefaults(0)
                priority = NotificationCompat.PRIORITY_LOW
                setContent(remoteViews)
            }
        startForeground(NOTIF_ID, notification?.build())
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        stopForeground(true)
        started = false
        cd.clear()
        super.onDestroy()
    }

    companion object {
        fun getNewIntent(context: Context) = Intent(context, ServiceCar::class.java)
    }

    inner class MyBinder : Binder() {
        fun getService() = this@ServiceCar
    }

    fun dismissNotification() {
        remoteViews = null
        notificationManager.cancelAll()
        stopForeground(true)
    }


}