package com.martial.soundplay.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.martial.soundplay.R
import com.martial.soundplay.data.Sound

object AudioPlayerManager {
    var currentSound: Sound? = null
        private set
    var isPlaying: Boolean = false
        private set

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    var onStateChanged: (() -> Unit)? = null
    var onProgressChanged: ((current: Int, total: Int) -> Unit)? = null

    fun play(context: Context, sound: Sound) {
        if (currentSound?.id == sound.id && mediaPlayer != null) {
            // Same sound, just toggle
            togglePlayPause()
            return
        }
        stop()
        currentSound = sound
        mediaPlayer = MediaPlayer.create(context, R.raw.the_mountain_smooth)?.apply {
            setOnCompletionListener {
                this@AudioPlayerManager.isPlaying = false
                onStateChanged?.invoke()
            }
            setLooping(true)
            start()
        }
        isPlaying = true
        onStateChanged?.invoke()
        startProgressUpdater()
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            } else {
                it.start()
                isPlaying = true
                startProgressUpdater()
            }
            onStateChanged?.invoke()
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0
    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        currentSound = null
    }

    private fun startProgressUpdater() {
        handler.post(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        onProgressChanged?.invoke(it.currentPosition, it.duration)
                        handler.postDelayed(this, 500)
                    }
                }
            }
        })
    }
}
