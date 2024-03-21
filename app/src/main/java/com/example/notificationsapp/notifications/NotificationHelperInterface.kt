package com.example.notificationsapp.notifications

import android.content.Context
import androidx.core.app.NotificationCompat

interface NotificationHelperInterface {
    fun notify(context: Context, notificationId: Int, notificationBuilder: NotificationCompat.Builder)

    fun cancel(context: Context, notificationId: Int)

    fun clearAll(context: Context)

    fun update(context: Context, notificationId: Int, notificationBuilder: NotificationCompat.Builder)
}