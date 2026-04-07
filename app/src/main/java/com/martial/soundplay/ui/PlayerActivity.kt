package com.martial.soundplay.ui

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.martial.soundplay.R
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var userSeeking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener { finish() }

        binding.btnPlayPause.setOnClickListener {
            AudioPlayerManager.togglePlayPause()
            updatePlayButton()
            updatePulse()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) binding.tvCurrent.text = formatTime(progress)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) { userSeeking = true }
            override fun onStopTrackingTouch(sb: SeekBar?) {
                userSeeking = false
                sb?.progress?.let { AudioPlayerManager.seekTo(it) }
            }
        })

        AudioPlayerManager.onProgressChanged = { current, total ->
            runOnUiThread {
                if (!userSeeking) {
                    binding.seekBar.max = total
                    binding.seekBar.progress = current
                    binding.tvCurrent.text = formatTime(current)
                    binding.tvTotal.text = formatTime(total)
                }
            }
        }

        // Init UI
        val sound = AudioPlayerManager.currentSound
        if (sound != null) {
            binding.playerIcon.setImageResource(sound.iconResId)
            binding.seekBar.max = AudioPlayerManager.getDuration()
            binding.tvTotal.text = formatTime(AudioPlayerManager.getDuration())
        }
        updatePlayButton()
        updatePulse()

        // Apply theme
        val theme = com.martial.soundplay.ThemeManager.get(this)
        binding.btnPlayPause.background.setTint(theme.primary)
        binding.seekBar.progressTintList = android.content.res.ColorStateList.valueOf(theme.primary)
        binding.seekBar.thumbTintList = android.content.res.ColorStateList.valueOf(theme.primaryContainer)
    }

    private fun updatePlayButton() {
        binding.btnPlayPause.setImageResource(
            if (AudioPlayerManager.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )
    }

    private fun updatePulse() {
        if (AudioPlayerManager.isPlaying) {
            binding.pulseView.startPulsing()
        } else {
            binding.pulseView.stopPulsing()
        }
    }

    private fun formatTime(ms: Int): String {
        val s = ms / 1000
        return String.format("%d:%02d", s / 60, s % 60)
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioPlayerManager.onProgressChanged = null
    }
}
