package com.martial.soundplay.audio

import com.martial.soundplay.data.Sound

object AudioPlayerManager {
    var currentSound: Sound? = null
        private set
    var isPlaying: Boolean = false
        private set

    var onStateChanged: (() -> Unit)? = null

    fun play(sound: Sound) {
        currentSound = sound
        isPlaying = true
        onStateChanged?.invoke()
    }

    fun togglePlayPause() {
        isPlaying = !isPlaying
        onStateChanged?.invoke()
    }

    fun stop() {
        isPlaying = false
        currentSound = null
        onStateChanged?.invoke()
    }
}
