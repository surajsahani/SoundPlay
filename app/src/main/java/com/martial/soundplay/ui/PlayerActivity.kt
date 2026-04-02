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
    private val audioManager = AudioPlayerManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener { finish() }

        binding.btnPlayPause.setOnClickListener { audioManager.togglePlayPause() }

        binding.btnNext.setOnClickListener { audioManager.skipNext(this) }

        binding.btnPrev.setOnClickListener { audioManager.skipPrevious(this) }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) audioManager.seekTo(progress)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Observe current sound
        audioManager.currentSound.observe(this) { sound ->
            sound?.let {
                binding.playerTitle.text = it.name
                binding.playerTags.text = it.tags
                binding.playerIcon.setImageResource(it.iconResId)
            }
        }

        // Observe play state
        audioManager.isPlaying.observe(this) { playing ->
            binding.btnPlayPause.setImageResource(
                if (playing) R.drawable.ic_pause else R.drawable.ic_play
            )
        }

        // Observe progress
        audioManager.duration.observe(this) { duration ->
            binding.seekBar.max = duration
            binding.tvTotalTime.text = formatTime(duration)
        }

        audioManager.progress.observe(this) { progress ->
            binding.seekBar.progress = progress
            binding.tvCurrentTime.text = formatTime(progress)
        }
    }

    private fun formatTime(ms: Int): String {
        val totalSeconds = ms / 1000
        val min = totalSeconds / 60
        val sec = totalSeconds % 60
        return String.format("%d:%02d", min, sec)
    }
}
