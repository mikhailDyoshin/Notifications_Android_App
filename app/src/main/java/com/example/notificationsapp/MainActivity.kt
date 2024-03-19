package com.example.notificationsapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsapp.common.NOTIFICATION_ID
import com.example.notificationsapp.notifications.NotificationHelper
import com.example.notificationsapp.notifications.NotificationType
import com.example.notificationsapp.notifications.NotificationsStore
import com.example.notificationsapp.presentation.NotificationsLauncherScreen
import com.example.notificationsapp.ui.theme.NotificationsAppTheme

class MainActivity : ComponentActivity() {

    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val basicNotification = NotificationsStore().getNotification(context, NotificationType.BASIC)
        val replyNotification = NotificationsStore().getNotification(context, NotificationType.REPLY)

        setContent {
            NotificationsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationsLauncherScreen(
                        notifyBasic = { NotificationHelper.notify(context, 11, basicNotification) },
                        notifyReply = { NotificationHelper.notify(context, 15, replyNotification) }
                    )
                }
            }
        }
    }

}
