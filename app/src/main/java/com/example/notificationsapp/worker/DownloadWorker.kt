package com.example.notificationsapp.worker

import android.content.Context
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.notificationsapp.R
import com.example.notificationsapp.common.DOWNLOAD_NOTIFICATION_ID
import com.example.notificationsapp.common.PROGRESS_MAX
import com.example.notificationsapp.notifications.NotificationsStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class DownloadWorker(private val context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {

        val initialNotification = NotificationsStore().getDownloadNotification(
            context = context,
            contentText = "Starting Download",
            progressCurrent = 0
        )
        setForeground(createForegroundInfo(initialNotification))
        download()
        return Result.success()
    }

    private suspend fun download() {
        var progress = 1

        val intent = WorkManager.getInstance(context)
            .createCancelPendingIntent(id)

        while (progress <= PROGRESS_MAX) {
            runBlocking { delay(100) }

            val notificationCurrent = NotificationsStore().getDownloadNotification(
                context = context,
                contentText = "Downloading",
                progressCurrent = progress
            ).addAction(androidx.core.R.drawable.ic_call_decline, "Cancel", intent)
            setForeground(createForegroundInfo(notificationCurrent))
            progress += 1
        }

        val notificationFinal = NotificationsStore().getDownloadNotification(
            context = context,
            contentText = "Download complete",
            progressCurrent = 0
        ).setProgress(0, 0, false)

        setForeground(createForegroundInfo(notificationFinal))
    }

    private fun createForegroundInfo(notification: NotificationCompat.Builder): ForegroundInfo {
        return ForegroundInfo(
            DOWNLOAD_NOTIFICATION_ID,
            notification.build(),
            FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

}