package com.martial.soundplay

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.martial.soundplay.databinding.ActivityMainBinding
import java.util.*

const val UPDATE_REQUEST_CODE = 524


class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var binding : ActivityMainBinding
    private lateinit var inAppUpdate: InAppUpdate

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultLauncher ->
            if (resultLauncher.resultCode == RESULT_OK) {
            }
        }
    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initialize()
        //inAppUpdate = InAppUpdate(this)

        checkUpdate()
    }

    private fun checkUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .setAllowAssetPackDeletion(true)
                        .build(),
                    UPDATE_REQUEST_CODE
                )
                resultLauncher.launch(intent)
            } else {
                Toast.makeText(this, "No Update Available", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdate.onActivityResult(requestCode,resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.onDestroy()
    }

}
