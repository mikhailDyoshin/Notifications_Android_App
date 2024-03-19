package com.example.notificationsapp.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.notificationsapp.MainActivity
import com.example.notificationsapp.R
import com.example.notificationsapp.broadcastReceiver.ReplyNotificationReceiver
import com.example.notificationsapp.common.CHANNEL_NAME
import com.example.notificationsapp.common.KEY_TEXT_REPLY
import com.example.notificationsapp.common.REPLY_LABEL

class NotificationsStore {

    fun getNotification(context: Context, notificationType: NotificationType): NotificationCompat.Builder {

        return when (notificationType) {
            NotificationType.BASIC -> {
                getBasicNotification(context)
            }

            NotificationType.REPLY -> {
                getReplyNotification(context)
            }
        }
    }

    private fun getBasicNotification(context: Context): NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, CHANNEL_NAME)
            .setSmallIcon(androidx.core.R.drawable.notification_bg_normal)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Much longer text that cannot fit one line. Hello, here is the second line of the text."
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    private fun getReplyNotification(context: Context): NotificationCompat.Builder {

        // Key for the string that's delivered in the action's intent.
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(REPLY_LABEL)
            build()
        }

        val resultIntent = Intent(context, ReplyNotificationReceiver::class.java)

        val replyPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context,
                0,
                resultIntent,
                PendingIntent.FLAG_MUTABLE)

        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_input_add,
            "Add", replyPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        return NotificationCompat.Builder(context, CHANNEL_NAME)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Got new ideas? Add Here!")
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .addAction(replyAction)
    }

}