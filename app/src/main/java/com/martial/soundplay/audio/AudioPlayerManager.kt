package com.martial.soundplay.audio

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.martial.soundplay.data.Sound
import com.martial.soundplay.data.SoundRepository

class AudioPlayerManager private constructor() {

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    private val _currentSound = MutableLiveData<Sound?>()
    val currentSound: LiveData<Sound?> = _currentSound

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _duration = MutableLiveData(0)
    val duration: LiveData<Int> = _duration

    private var timerEndTime: Long = 0L
    private val _timerRemaining = MutableLiveData(0L)
    val timerRemaining: LiveData<Long> = _timerRemaining

    companion object {
        @Volatile
        private var instance: AudioPlayerManager? = null
        fun getInstance(): AudioPlayerManager =
            instance ?: synchronized(this) {
                instance ?: AudioPlayerManager().also { instance = it }
            }
    }

    fun play(context: Context, sound: Sound) {
        stop()
        _currentSound.value = sound
        mediaPlayer = MediaPlayer.create(context, sound.rawResId)?.apply {
            setOnCompletionListener {
                skipNext(context)
            }
            setOnErrorListener { _, _, _ ->
                _isPlaying.value = false
                true
            }
            start()
            _isPlaying.value = true
            _duration.value = duration
            startProgressUpdater()
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            } else {
                it.start()
                _isPlaying.value = true
                startProgressUpdater()
            }
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _progress.value = position
    }

    fun skipNext(context: Context) {
        val current = _currentSound.value ?: return
        val sounds = SoundRepository.sounds
        val idx = sounds.indexOfFirst { it.id == current.id }
        val next = sounds[(idx + 1) % sounds.size]
        play(context, next)
    }

    fun skipPrevious(context: Context) {
        val current = _currentSound.value ?: return
        val sounds = SoundRepository.sounds
        val idx = sounds.indexOfFirst { it.id == current.id }
        val prev = sounds[if (idx - 1 < 0) sounds.size - 1 else idx - 1]
        play(context, prev)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _progress.value = 0
    }

    fun setTimer(minutes: Int) {
        timerEndTime = System.currentTimeMillis() + minutes * 60 * 1000L
        startTimerUpdater()
    }

    fun cancelTimer() {
        timerEndTime = 0L
        _timerRemaining.value = 0L
    }

    private fun startProgressUpdater() {
        handler.post(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _progress.postValue(it.currentPosition)
                        handler.postDelayed(this, 250)
                    }
                }
            }
        })
    }

    private fun startTimerUpdater() {
        handler.post(object : Runnable {
            override fun run() {
                val remaining = timerEndTime - System.currentTimeMillis()
                if (remaining > 0) {
                    _timerRemaining.postValue(remaining)
                    handler.postDelayed(this, 1000)
                } else if (timerEndTime > 0) {
                    _timerRemaining.postValue(0L)
                    stop()
                    timerEndTime = 0L
                }
            }
        })
    }

    fun release() {
        stop()
        _currentSound.value = null
        _duration.value = 0
    }
}
