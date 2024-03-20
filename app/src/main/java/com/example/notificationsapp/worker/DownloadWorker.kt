package com.example.notificationsapp.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.notificationsapp.common.DOWNLOAD_NOTIFICATION_ID
import com.example.notificationsapp.common.PROGRESS_MAX
import com.example.notificationsapp.notifications.NotificationsStore
import kotlinx.coroutines.delay

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
        var progress = 0

        while (progress < PROGRESS_MAX) {
            delay(1000)
            progress++
            val notificationCurrent = NotificationsStore().getDownloadNotification(
                context = context,
                contentText = "Starting Download",
                progressCurrent = 0
            )
            setForeground(createForegroundInfo(notificationCurrent))
        }
    }

    private fun createForegroundInfo(notification: NotificationCompat.Builder): ForegroundInfo {
        return ForegroundInfo(DOWNLOAD_NOTIFICATION_ID, notification.build())
    }

}