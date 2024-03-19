package com.example.notificationsapp.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.notificationsapp.common.CHANNEL_NAME

class NotificationsStore {

    fun getNotification(context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_NAME)
            .setSmallIcon(androidx.core.R.drawable.notification_bg_normal)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line. Hello, here is the second line of the text.")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

}