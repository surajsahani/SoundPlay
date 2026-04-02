package com.martial.soundplay.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.martial.soundplay.R
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener { finish() }
        binding.btnPlayPause.setOnClickListener {
            AudioPlayerManager.togglePlayPause()
            updatePlayButton()
        }

        val sound = AudioPlayerManager.currentSound
        if (sound != null) {
            binding.playerIcon.setImageResource(sound.iconResId)
        }
        updatePlayButton()
    }

    private fun updatePlayButton() {
        binding.btnPlayPause.setImageResource(
            if (AudioPlayerManager.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
    }
}
