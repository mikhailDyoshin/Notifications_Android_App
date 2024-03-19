package com.example.notificationsapp.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.notificationsapp.common.KEY_TEXT_REPLY
import com.example.notificationsapp.common.NOTIFICATION_ID

class ReplyNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        if (remoteInput != null) {
            val title = remoteInput.getCharSequence(
                KEY_TEXT_REPLY
            ).toString()
            Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
        }
    }
}