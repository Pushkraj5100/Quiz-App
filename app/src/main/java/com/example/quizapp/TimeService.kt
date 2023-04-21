package com.example.quizapp

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


class TimeService: Service() {
    private lateinit var countDownTimer: CountDownTimer
    var counter: Long = -1
    companion object{
        const val PACKAGE = "com.example.quizapp"
        const val KEY = "countdown"
        const val FINISH_KEY = "countdown_finish"
        const val CHANNEL_ID = "channel1"
    }
    val intent: Intent = Intent(PACKAGE)
    override fun onBind(p0: Intent?): IBinder? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel( CHANNEL_ID, "channel1", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        countDownTimer = object : CountDownTimer(600000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter = millisUntilFinished
                intent.putExtra(KEY,millisUntilFinished)
                sendBroadcast(intent)
            }

            override fun onFinish() {
                intent.putExtra(FINISH_KEY,true)
                sendBroadcast(intent)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        countDownTimer.start()
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID).build()
        startForeground(1, notification)
        updateNotification()
        return START_NOT_STICKY
    }

    private fun updateNotification() {
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Quiz App")
                .setContentText("Quiz is running")
                .setSmallIcon(R.drawable.sym_def_app_icon)
                .build()
            startForeground(1, notification)
    }
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}