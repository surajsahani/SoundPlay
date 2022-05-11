package com.martialcoder.soundplay

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toast.makeText(this, "Touch your inner space", Toast.LENGTH_SHORT).show()

        buttonOne.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonOne.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.first)
            mediaPlayer?.start()
        }
        buttonTwo.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonTwo.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_3)
            mediaPlayer?.start()
        }
        buttonThree.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonThree.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_4)
            mediaPlayer?.start()
        }
        buttonFour.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonFour.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.third)
            mediaPlayer?.start()
        }
        buttonFive.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonFive.setBackgroundColor(color)

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_5)
            mediaPlayer?.start()
        }
        buttonSix.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonSix.setBackgroundColor(color)


            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_6)
            mediaPlayer?.start()
        }
        buttonSeven.setOnClickListener {

            // generate a new random number
            val random = Random()

            // generate a background color using RBG
            val color =
                Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

            // set layout's background color
            buttonSeven.setBackgroundColor(color)


            mediaPlayer = MediaPlayer.create(this, R.raw.seven)
            mediaPlayer?.start()
        }
        // Prepare asynchronously to not block the Main Thread


    }

}
