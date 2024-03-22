package com.example.notificationsapp

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.notificationsapp.common.BASIC_NOTIFICATION_ID
import com.example.notificationsapp.common.REPLY_NOTIFICATION_ID
import com.example.notificationsapp.notifications.NotificationHelper
import com.example.notificationsapp.notifications.NotificationType
import com.example.notificationsapp.notifications.NotificationsStore
import com.example.notificationsapp.presentation.NotificationsLauncherScreen
import com.example.notificationsapp.service.PlaybackService
import com.example.notificationsapp.ui.theme.NotificationsAppTheme
import com.example.notificationsapp.worker.DownloadWorker
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : ComponentActivity() {

    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val basicNotification =
            NotificationsStore().getNotification(context, NotificationType.BASIC)
        val replyNotification =
            NotificationsStore().getNotification(context, NotificationType.REPLY)

        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))

        val audioUrl = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"

        val mediaItem = MediaItem.fromUri(Uri.parse(audioUrl))

        val controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()




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
                            val uploadWorkRequest: WorkRequest =
                                OneTimeWorkRequestBuilder<DownloadWorker>()
                                    .build()

                            WorkManager
                                .getInstance(context)
                                .enqueue(uploadWorkRequest)
                        },
                        play = {
                            controllerFuture.addListener({
                                val controller = controllerFuture.get()
                                controller.setMediaItem(mediaItem)
                                controller.prepare()
                                controller.play()
                            }, MoreExecutors.directExecutor())

                        },
                        pause = {
                            controllerFuture.addListener({
                                val controller = controllerFuture.get()
                                controller.pause()
                            }, MoreExecutors.directExecutor())

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
