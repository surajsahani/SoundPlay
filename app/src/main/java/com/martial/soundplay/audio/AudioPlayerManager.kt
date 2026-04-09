package com.martial.soundplay.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import com.martial.soundplay.data.Sound

object AudioPlayerManager {

    // Multiple players for sound mixing
    private val activePlayers = mutableMapOf<Int, MediaPlayer>()
    private val activeVolumes = mutableMapOf<Int, Float>() // per-sound volume 0..1
    private val activeSounds = mutableMapOf<Int, Sound>()

    private val handler = Handler(Looper.getMainLooper())
    var onStateChanged: (() -> Unit)? = null
    var onTimerTick: ((remaining: Long) -> Unit)? = null
    var onTimerFinish: (() -> Unit)? = null

    private var sleepTimer: CountDownTimer? = null
    var timerRemaining: Long = 0L
        private set

    val isPlaying: Boolean get() = activePlayers.any { it.value.isPlaying }
    val currentSound: Sound? get() = activeSounds.values.firstOrNull()
    val playingSounds: List<Sound> get() = activeSounds.values.toList()

    fun isPlayingSound(soundId: Int): Boolean = activePlayers.containsKey(soundId)

    fun getVolume(soundId: Int): Float = activeVolumes[soundId] ?: 0.7f

    fun toggleSound(context: Context, sound: Sound) {
        if (activePlayers.containsKey(sound.id)) {
            stopSound(sound.id)
        } else {
            playSound(context, sound)
        }
        onStateChanged?.invoke()
    }

    private fun playSound(context: Context, sound: Sound) {
        val player = MediaPlayer.create(context, sound.rawResId) ?: return
        player.setLooping(true)
        val vol = 0.7f
        player.setVolume(vol, vol)
        player.start()
        activePlayers[sound.id] = player
        activeSounds[sound.id] = sound
        activeVolumes[sound.id] = vol
    }

    private fun stopSound(id: Int) {
        activePlayers[id]?.let {
            it.stop()
            it.release()
        }
        activePlayers.remove(id)
        activeSounds.remove(id)
        activeVolumes.remove(id)
    }

    fun setVolume(soundId: Int, volume: Float) {
        val v = volume.coerceIn(0f, 1f)
        activeVolumes[soundId] = v
        activePlayers[soundId]?.setVolume(v, v)
    }

    fun stopAll() {
        activePlayers.values.forEach {
            it.stop()
            it.release()
        }
        activePlayers.clear()
        activeSounds.clear()
        activeVolumes.clear()
        cancelTimer()
        onStateChanged?.invoke()
    }

    // Sleep timer
    fun startTimer(minutes: Int) {
        cancelTimer()
        val millis = minutes * 60 * 1000L
        sleepTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(remaining: Long) {
                timerRemaining = remaining
                onTimerTick?.invoke(remaining)
            }
            override fun onFinish() {
                timerRemaining = 0
                onTimerFinish?.invoke()
                stopAll()
            }
        }.start()
    }

    fun cancelTimer() {
        sleepTimer?.cancel()
        sleepTimer = null
        timerRemaining = 0
    }

    val hasTimer: Boolean get() = timerRemaining > 0

    // Legacy compat for player screen
    fun play(context: Context, sound: Sound) {
        if (!isPlayingSound(sound.id)) {
            playSound(context, sound)
            onStateChanged?.invoke()
        }
    }

    fun togglePlayPause() {
        if (isPlaying) {
            activePlayers.values.forEach { if (it.isPlaying) it.pause() }
        } else {
            activePlayers.values.forEach { it.start() }
        }
        onStateChanged?.invoke()
    }

    fun seekTo(position: Int) {
        activePlayers.values.firstOrNull()?.seekTo(position)
    }

    fun getDuration(): Int = activePlayers.values.firstOrNull()?.duration ?: 0
    fun getCurrentPosition(): Int = activePlayers.values.firstOrNull()?.currentPosition ?: 0

    var onProgressChanged: ((current: Int, total: Int) -> Unit)? = null

    fun startProgressUpdater() {
        handler.post(object : Runnable {
            override fun run() {
                val player = activePlayers.values.firstOrNull()
                if (player != null && player.isPlaying) {
                    onProgressChanged?.invoke(player.currentPosition, player.duration)
                    handler.postDelayed(this, 500)
                }
            }
        })
    }
}
