package com.example.hicabbie.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log.d
import android.widget.RemoteViews
import com.example.hicabbie.R

class ServiceCar : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val TAG = ServiceCar::class.java.simpleName

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        d(TAG, " onStartCommand")
        showNotification()
        fetch()
        return START_NOT_STICKY
    }

    val handler = Handler()

    inner class FakeService : Runnable {
        override fun run() {
            d(TAG, " FAKE SERVICE")
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed(this, 5000)
        }
    }


    private fun fetch() {
        val handler = Handler()
        handler.postDelayed(FakeService(), 5000)
    }

    private fun showNotification() {

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

        val remoteViews = RemoteViews(context.packageName, R.layout.notify_row)
        remoteViews.setTextViewText(R.id.tvTitle, "Hi Cabbie")
//      Notification
        val notification = NotificationCompat.Builder(context, channelID)
            .apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setOngoing(true)
                setTicker("title")
                setDefaults(0)
                priority = NotificationCompat.PRIORITY_LOW
                setContent(remoteViews)
            }
        notification.build()
        startForeground(1103, notification.build())
    }

    override fun onDestroy() {
        stopForeground(true)
        started = false
        super.onDestroy()
    }

    companion object {
        fun getNewIntent(context: Context) = Intent(context, ServiceCar::class.java)
        var started = false
    }
}