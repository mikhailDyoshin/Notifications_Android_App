package com.example.notificationsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.notificationsapp.common.BASIC_NOTIFICATION_ID
import com.example.notificationsapp.common.REPLY_NOTIFICATION_ID
import com.example.notificationsapp.notifications.NotificationHelper
import com.example.notificationsapp.notifications.NotificationType
import com.example.notificationsapp.notifications.NotificationsStore
import com.example.notificationsapp.presentation.NotificationsLauncherScreen
import com.example.notificationsapp.ui.theme.NotificationsAppTheme
import com.example.notificationsapp.worker.DownloadWorker

class MainActivity : ComponentActivity() {

    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val basicNotification =
            NotificationsStore().getNotification(context, NotificationType.BASIC)
        val replyNotification =
            NotificationsStore().getNotification(context, NotificationType.REPLY)

        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<DownloadWorker>()
                .build()

        setContent {
            NotificationsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationsLauncherScreen(
                        notifyBasic = {
                            NotificationHelper.notify(
                                context,
                                BASIC_NOTIFICATION_ID,
                                basicNotification
                            )
                        },
                        notifyReply = {
                            NotificationHelper.notify(
                                context,
                                REPLY_NOTIFICATION_ID,
                                replyNotification
                            )
                        },
                        notifyDownload = {
                            WorkManager
                                .getInstance(context)
                                .enqueue(uploadWorkRequest)
                        },
                        clearAll = {
                            NotificationHelper.clearAll(context)
                        }
                    )
                }
            }
        }
    }

}
