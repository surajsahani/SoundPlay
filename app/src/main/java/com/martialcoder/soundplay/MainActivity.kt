package com.martialcoder.soundplay

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Touch your inner space", Toast.LENGTH_SHORT).show()

        buttonOne.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.first)
            mediaPlayer?.start()
        }
        buttonTwo.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_3)
            mediaPlayer?.start()
        }
        buttonThree.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_4)
            mediaPlayer?.start()
        }
        buttonFour.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.third)
            mediaPlayer?.start()
        }
        buttonFive.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_5)
            mediaPlayer?.start()
        }
        buttonSix.setOnClickListener {


            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_6)
            mediaPlayer?.start()
        }
        buttonSeven.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.seven)
            mediaPlayer?.start()
        }
        // Prepare asynchronously to not block the Main Thread


    }

}
