package com.duartefigueiredo.foregroundservice

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout


class ForegroundService : View.OnTouchListener, Service() {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    companion object {
        const val CHANNEL_ID = "1"
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setSmallIcon(R.drawable.button_onoff_indicator_on)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        //do heavy work on a background thread
        startTouchWindow()

        //stopSelf();

        return START_NOT_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun startTouchWindow() {
        val touchLayout = LinearLayout(this)

        // set layout width 30 px and height is equal to full screen
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        touchLayout.layoutParams = lp
        // set color if you want layout visible on screen
        //touchLayout.setBackgroundColor(Color.CYAN);
        // set on touch listener
        touchLayout.setOnTouchListener(this)

        // fetch window manager object
        val mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        // set layout parameter of window manager
        val mParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, // width is equal to full screen
            WindowManager.LayoutParams.MATCH_PARENT, // height is equal to full screen
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Type Phone, These are non-application windows providing user interaction with the phone (in particular incoming calls).
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // this window won't ever get key input focus
            PixelFormat.RGBA_8888
        )
        mParams.gravity = Gravity.LEFT or Gravity.TOP
        //Log.i(TAG, "add View")
        val a = kotlin.runCatching {
            mWindowManager.addView(touchLayout, mParams)
        }

        if(a.isFailure){
            a.exceptionOrNull()?.printStackTrace()
        }
    }
}