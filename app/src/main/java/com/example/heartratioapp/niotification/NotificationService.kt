package com.example.heartratioapp.niotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.heartratioapp.MainActivity
import com.example.heartratioapp.R
import com.example.heartratioapp.settings_activity.Settings
import java.lang.Thread.sleep
import java.time.LocalTime
import kotlin.concurrent.thread

class NotificationService : Service() {
    private val CHANNEL_ID: String = "notifyHeartRate"
    private lateinit var builder: NotificationCompat.Builder
    private var notified = false

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        builder = createNotificationBuilder()

        thread {
            while (true) {
                sleep(1000)
                if (Settings.notifyOn and !notified) {
                    if (LocalTime.now() == Settings.notifyTime) {
                        with(NotificationManagerCompat.from(this)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(1, builder.build())
                        }
                        notified = true
                    }
                } else {
                    sleep(60000)
                    notified = false
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notify_title))
            .setContentText(getString(R.string.notify_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
}