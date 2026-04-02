package com.martial.soundplay

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.martial.soundplay.databinding.ActivityMainBinding
import java.util.*



class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initialize()
    }

    private fun playSound(resId: Int) {
        if (isPlaying) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        isPlaying = true
        mediaPlayer = MediaPlayer.create(this, resId)
        mediaPlayer?.setOnCompletionListener {
            isPlaying = false
            it.release()
            mediaPlayer = null
        }
        mediaPlayer?.setOnErrorListener { mp, _, _ ->
            isPlaying = false
            mp.release()
            mediaPlayer = null
            true
        }
        mediaPlayer?.start()
    }

    private fun initialize() {
        binding.buttonOne.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.first)
        }
        binding.buttonTwo.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.sound_file_3)
        }
        binding.buttonThree.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.sound_file_4)
        }
        binding.buttonFour.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.third)
        }
        binding.buttonFive.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.sound_file_5)
        }
        binding.buttonSix.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.sound_file_6)
        }
        binding.buttonSeven.setOnClickListener {
            it.setBackgroundColor(randomColor())
            playSound(R.raw.seven)
        }
    }

    private fun randomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}
