package com.duartefigueiredo.foregroundservice

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private lateinit var startForegroundButton: Button
    private lateinit var stopForegroundButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startForegroundButton = findViewById(R.id.startForegroundService)
        startForegroundButton.setOnClickListener { startForegroundService() }

        stopForegroundButton = findViewById(R.id.stopForegroundService)
        stopForegroundButton.setOnClickListener { stopForegroundService() }
    }

    private fun startForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)

        ContextCompat.startForegroundService(this, serviceIntent)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.v("MainActivity","screen was touched")

        return super.onTouchEvent(event)
    }

    private fun stopForegroundService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)

        stopService(serviceIntent)
    }
}
