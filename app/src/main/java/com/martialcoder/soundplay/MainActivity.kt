package com.martialcoder.soundplay

import android.database.DatabaseUtils
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.martialcoder.soundplay.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        binding.buttonOne.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonOne.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.first)
            mediaPlayer?.start()
        }
        binding.buttonTwo.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonTwo.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_3)
            mediaPlayer?.start()
        }
        binding.buttonThree.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonThree.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_4)
            mediaPlayer?.start()
        }
        binding.buttonFour.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonFour.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.third)
            mediaPlayer?.start()
        }
        binding.buttonFive.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonFive.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_5)
            mediaPlayer?.start()
        }
        binding.buttonSix.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonSix.setBackgroundColor(color)


            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_6)
            mediaPlayer?.start()
        }
        binding.buttonSeven.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            binding.buttonSeven.setBackgroundColor(color)


            mediaPlayer = MediaPlayer.create(this, R.raw.seven)
            mediaPlayer?.start()
        }
        // Prepare asynchronously to not block the Main Thread
    }

}
