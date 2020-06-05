package com.example.soundplay

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

        button14.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_1)
            mediaPlayer?.start()
        }
        button13.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_2)
            mediaPlayer?.start()
        }
        button12.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_3)
            mediaPlayer?.start()
        }
        button11.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_4)
            mediaPlayer?.start()
        }
        button10.setOnClickListener {

            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_5)
            mediaPlayer?.start()
        }
        button8.setOnClickListener {


            mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_6)
            mediaPlayer?.start()
        }
            button9.setOnClickListener {

                mediaPlayer = MediaPlayer.create(this, R.raw.sound_file_7)
                mediaPlayer?.start()
            }
         // Prepare asynchronously to not block the Main Thread



    }
    
    }
