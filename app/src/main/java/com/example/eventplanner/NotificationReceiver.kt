package com.example.eventplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.Manifest

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val eventName = intent.getStringExtra("eventName")

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val builder = NotificationCompat.Builder(context, "event_notifications")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Event Reminder")
                .setContentText("Don't forget about $eventName tomorrow!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder.build())
            }
        }
    }
}
