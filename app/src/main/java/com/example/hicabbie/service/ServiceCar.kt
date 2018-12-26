package com.example.hicabbie.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import com.example.hicabbie.data.api.Api
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
    private var lastLocationResponse: ResponseLocation? = null

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
        val type = intent?.getIntExtra(REQUEST_CODE, 0)
        Log.e(TAG, " code " + intent?.getIntExtra(REQUEST_CODE, 0))
        when (type) {
            88 -> stopSelf()
            else -> { }
        }
        return START_NOT_STICKY
    }

    fun fetch() {
        if (cd.isDisposed)
            cd = CompositeDisposable()
        if (started)
            cd.add(Observable
                .interval(15, TimeUnit.SECONDS)
                .startWith(0)
                .flatMap { api.getLocation().toObservable() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status.equals("success")) {
                        this.lastLocationResponse = it
                        updateListner?.update(it)
                        updateNotification()
                    }
                }, { updateNotification(msg = it.localizedMessage) })
            )
        else {
            cd.dispose()
            lastLocationResponse = null
        }

    }

    fun getLastLocation() = lastLocationResponse


    var updateListner: OnUpdateListner? = null
    fun updateListner(updateListner: OnUpdateListner?) {
        this.updateListner = updateListner
    }

    private fun updateNotification(msg: String = "") {
        remoteViews?.let { v ->
            if (!msg.isEmpty())
                v.setTextViewText(R.id.tvProgress, msg)
            else
                lastLocationResponse?.let { loc ->
                    v.setTextViewText(
                        R.id.tvProgress,
                        "location updates in progress - ${loc.latitude} - ${loc.longitude} "
                    )
                }
            notificationManager.notify(NOTIF_ID, notification?.build())
        }
    }

    private var remoteViews: RemoteViews? = null
    private var notification: NotificationCompat.Builder? = null

    private val REQUEST_CODE: String = "request_code"

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
            .apply {  }
        val remoteViews2 = RemoteViews(context.packageName, R.layout.notify_row_big)
        //stop service
        val stopIntent = Intent(context, ServiceCar::class.java)
            .apply { putExtra(REQUEST_CODE, 88) }
        val pIntent = PendingIntent.getService(context, 88, stopIntent, 0)

        //register clicks
        remoteViews?.apply {
            setOnClickPendingIntent(R.id.ivBtnStop, pIntent)
        }
        //register click
        remoteViews2.apply {
            setOnClickPendingIntent(R.id.ivBtnStop, pIntent)
        }

//      Notification
        notification = NotificationCompat.Builder(context, channelID)
            .apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setOngoing(true)
                setTicker("title")
                setDefaults(0)
                priority = NotificationCompat.PRIORITY_LOW
                setContent(remoteViews)
                setCustomBigContentView(remoteViews2)
            }
        startForeground(NOTIF_ID, notification?.build())
        updateNotification()
    }

    override fun onDestroy() {
        cd.clear()
        stopForeground(true)
        started = false
        lastLocationResponse = null
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