package com.example.notificationsapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import com.google.common.collect.ImmutableList

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    private lateinit var nBuilder: NotificationCompat.Builder

    // Create your player and media session in the onCreate lifecycle event
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()

        this.setMediaNotificationProvider(object : MediaNotification.Provider {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun createNotification(
                mediaSession: MediaSession,// this is the session we pass to style
                customLayout: ImmutableList<CommandButton>,
                actionFactory: MediaNotification.ActionFactory,
                onNotificationChangedCallback: MediaNotification.Provider.Callback
            ): MediaNotification {
                createNotification(mediaSession)
                // notification should be created before you return here
                return MediaNotification(123, nBuilder.build())
            }

            override fun handleCustomCommand(
                session: MediaSession,
                action: String,
                extras: Bundle
            ): Boolean {
                TODO("Not yet implemented")
            }
        })
    }

    // The user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            // Stop the service if not playing, continue playing in the background
            // otherwise.
            stopSelf()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_PLAY -> {
                    // Handle play action
                }
                ACTION_PAUSE -> {
                    Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show()
                }
                ACTION_PREVIOUS -> {
                    // Handle previous action
                }
                ACTION_NEXT -> {
                    // Handle next action
                }
                ACTION_CONTROL_SPEED -> {
                    // Handle control speed action
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    @OptIn(UnstableApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(session: MediaSession) {

        val repeatPendingIntent: PendingIntent? = null
        val prevPendingIntent: PendingIntent? = null
        val pausePendingIntent: PendingIntent? = PendingIntent.getService(
            this,
            0,
            Intent(this, PlaybackService::class.java).setAction(ACTION_PAUSE),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val nextPendingIntent: PendingIntent? = null
        val controlSpeed: PendingIntent? = null

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )

        // NotificationCompat.Builder here.
        nBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(androidx.media3.ui.R.drawable.exo_icon_vr)
            .addAction(
                androidx.media3.ui.R.drawable.exo_icon_repeat_all,
                "Repeat all",
                repeatPendingIntent
            )
            .addAction(
                androidx.media3.ui.R.drawable.exo_notification_previous,
                "Previous",
                prevPendingIntent
            ) // #0
            .addAction(
                androidx.media3.ui.R.drawable.exo_notification_pause,
                "Pause",
                pausePendingIntent
            ) // #1
            .addAction(
                androidx.media3.ui.R.drawable.exo_notification_next,
                "Next",
                nextPendingIntent
            ) // #2
            .addAction(
                androidx.media3.ui.R.drawable.exo_styled_controls_speed,
                "Control speed",
                controlSpeed
            )
            .setStyle(
                MediaStyleNotificationHelper.MediaStyle(session)
                    .setShowActionsInCompactView(2 /* #1: pause button \*/)
            )
    }

    companion object {
        private const val CHANNEL_ID = "PlaybackServiceChannel"
        const val ACTION_PLAY = "com.example.notificationsapp.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.notificationsapp.ACTION_PAUSE"
        const val ACTION_PREVIOUS = "com.example.notificationsapp.ACTION_PREVIOUS"
        const val ACTION_NEXT = "com.example.notificationsapp.ACTION_NEXT"
        const val ACTION_CONTROL_SPEED = "com.example.notificationsapp.ACTION_CONTROL_SPEED"
    }
}
