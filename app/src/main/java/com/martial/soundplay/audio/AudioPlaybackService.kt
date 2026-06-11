package com.martial.soundplay.audio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.martial.soundplay.MainActivity
import com.martial.soundplay.R

class AudioPlaybackService : Service() {

    private var mediaSession: MediaSessionCompat? = null
    private var isForeground = false

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "soundplay_playback_channel"

        const val ACTION_START = "com.martial.soundplay.action.START"
        const val ACTION_UPDATE = "com.martial.soundplay.action.UPDATE"
        const val ACTION_STOP = "com.martial.soundplay.action.STOP"

        const val ACTION_PLAY_PAUSE = "com.martial.soundplay.action.PLAY_PAUSE"
        const val ACTION_STOP_PLAYBACK = "com.martial.soundplay.action.STOP_PLAYBACK"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        // Create MediaSessionCompat
        mediaSession = MediaSessionCompat(this, "SoundPlayMediaSession").apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    AudioPlayerManager.togglePlayPause()
                }

                override fun onPause() {
                    AudioPlayerManager.togglePlayPause()
                }

                override fun onStop() {
                    AudioPlayerManager.stopAll()
                }
            })
            isActive = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            ACTION_START -> {
                updateForegroundState()
            }
            ACTION_UPDATE -> {
                updateForegroundState()
            }
            ACTION_STOP -> {
                stopForegroundService()
            }
            ACTION_PLAY_PAUSE -> {
                AudioPlayerManager.togglePlayPause()
            }
            ACTION_STOP_PLAYBACK -> {
                AudioPlayerManager.stopAll()
            }
        }

        return START_NOT_STICKY
    }

    private fun updateForegroundState() {
        val isPlaying = AudioPlayerManager.isPlaying
        val activeSounds = AudioPlayerManager.playingSounds

        if (activeSounds.isEmpty()) {
            stopForegroundService()
            return
        }

        // Update MediaSession PlaybackState
        val state = if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        val playbackState = PlaybackStateCompat.Builder()
            .setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 1.0f)
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_STOP)
            .build()
        mediaSession?.setPlaybackState(playbackState)

        // Build notification
        val notification = buildMediaNotification(isPlaying, activeSounds.joinToString(", ") { it.name })

        if (!isForeground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
            isForeground = true
        } else {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun buildMediaNotification(isPlaying: Boolean, soundsText: String): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Play/Pause Action
        val playPauseIntent = Intent(this, AudioPlaybackService::class.java).apply { action = ACTION_PLAY_PAUSE }
        val playPausePendingIntent = PendingIntent.getService(
            this, 1, playPauseIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Stop Action
        val stopIntent = Intent(this, AudioPlaybackService::class.java).apply { action = ACTION_STOP_PLAYBACK }
        val stopPendingIntent = PendingIntent.getService(
            this, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_filled
        val playPauseTitle = if (isPlaying) "Pause" else "Play"

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_nav_waves)
            .setContentTitle("SoundPlay Ambient Mix")
            .setContentText(soundsText)
            .setContentIntent(openAppPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(isPlaying)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1)
            )
            .addAction(playPauseIcon, playPauseTitle, playPausePendingIntent)
            .addAction(R.drawable.ic_close_x, "Stop", stopPendingIntent)

        // Use dynamic theme primary color for notification background accent
        mediaSession?.controller?.metadata?.let {
            // Can be expanded if metadata metadata is set
        }

        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Background Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification with playback controls for SoundPlay background audio mixing"
                setShowBadge(false)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun stopForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        isForeground = false
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.run {
            isActive = false
            release()
        }
        mediaSession = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
