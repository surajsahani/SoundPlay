package com.martial.soundplay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.databinding.ActivityMainBinding
import com.martial.soundplay.ui.PlayerActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val audioManager = AudioPlayerManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHostFragment.navController)

        // Mini player controls
        binding.miniPlayer.miniPlayBtn.setOnClickListener {
            audioManager.togglePlayPause()
        }

        binding.miniPlayer.miniCloseBtn.setOnClickListener {
            audioManager.release()
            binding.miniPlayer.root.visibility = View.GONE
        }

        binding.miniPlayer.root.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }

        // Observe playback state for mini player
        audioManager.currentSound.observe(this) { sound ->
            sound?.let {
                binding.miniPlayer.miniTitle.text = it.name
                binding.miniPlayer.miniTags.text = it.tags
                binding.miniPlayer.miniIcon.setImageResource(it.iconResId)
            }
        }

        audioManager.isPlaying.observe(this) { playing ->
            binding.miniPlayer.miniPlayBtn.setImageResource(
                if (playing) R.drawable.ic_pause else R.drawable.ic_play
            )
        }
    }

    fun showMiniPlayer() {
        binding.miniPlayer.root.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        audioManager.release()
    }
}
