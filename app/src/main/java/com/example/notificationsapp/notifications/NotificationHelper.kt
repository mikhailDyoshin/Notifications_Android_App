package com.example.notificationsapp.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper : NotificationHelperInterface {

    private val notificationIds = mutableSetOf<Int>()

    override fun notify(
        context: Context,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder
    ) {
        if (notificationIds.contains(notificationId)) {
            throw IllegalArgumentException("Notification with ID $notificationId already exists.")
        }

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
        notificationIds.add(notificationId)
    }

    override fun cancel(context: Context, notificationId: Int) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)
        notificationIds.remove(notificationId)
    }

    override fun clearAll(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationIds.forEach { notificationManager.cancel(it) }
        notificationIds.clear()
    }

    override fun update(
        context: Context,
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder
    ) {
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


}