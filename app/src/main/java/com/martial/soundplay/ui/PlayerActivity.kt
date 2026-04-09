package com.martial.soundplay.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.martial.soundplay.R
import com.martial.soundplay.ThemeManager
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.data.Sound
import com.martial.soundplay.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val dp by lazy { resources.displayMetrics.density }
    private var timerTotalMs: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val theme = ThemeManager.get(this)

        binding.btnClose.setOnClickListener { finish() }
        binding.btnStopAll.setOnClickListener {
            AudioPlayerManager.stopAll()
            finish()
        }
        binding.btnPlayPause.setOnClickListener {
            AudioPlayerManager.togglePlayPause()
            updateUI()
        }
        binding.btnPlayPause.background.setTint(theme.primary)

        binding.btnTimer15.setOnClickListener { setTimer(15) }
        binding.btnTimer30.setOnClickListener { setTimer(30) }
        binding.btnTimer45.setOnClickListener { setTimer(45) }
        binding.btnTimer60.setOnClickListener { setTimer(60) }

        AudioPlayerManager.onTimerTick = { remaining -> runOnUiThread {
            val min = remaining / 60000
            binding.tvTimerDisplay.text = "${min}m"
            if (timerTotalMs > 0) {
                binding.timerRing.progress = remaining.toFloat() / timerTotalMs
            }
        }}
        AudioPlayerManager.onTimerFinish = { runOnUiThread { finish() } }
        AudioPlayerManager.onStateChanged = { runOnUiThread { updateUI() } }

        updateUI()
        updateTimerDisplay()
    }

    private fun updateUI() {
        val sounds = AudioPlayerManager.playingSounds
        binding.btnPlayPause.setImageResource(
            if (AudioPlayerManager.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        )

        // Rebuild mixer cards
        binding.mixerList.removeAllViews()
        val theme = ThemeManager.get(this)

        if (sounds.isEmpty()) {
            binding.mixerList.addView(TextView(this).apply {
                text = "Tap sounds on the home screen to start mixing"
                setTextColor(getColor(R.color.on_surface_variant))
                textSize = 13f
                gravity = Gravity.CENTER
                setPadding(0, d(40), 0, d(40))
            })
            return
        }

        sounds.forEach { sound ->
            binding.mixerList.addView(buildSoundCard(sound, theme))
        }
    }

    private fun buildSoundCard(sound: Sound, theme: ThemeManager.AppTheme): LinearLayout {
        val volPercent = (AudioPlayerManager.getVolume(sound.id) * 100).toInt()

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.bg_first_sound)
            setPadding(d(16), d(14), d(16), d(14))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = d(10) }
        }

        // Top row: icon + name + tags + volume %
        val topRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val iconFrame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(40), d(40))
            setBackgroundResource(R.drawable.bg_icon_circle)
        }
        iconFrame.addView(ImageView(this).apply {
            setImageResource(sound.iconResId)
            layoutParams = FrameLayout.LayoutParams(d(20), d(20)).apply { gravity = Gravity.CENTER }
        })
        topRow.addView(iconFrame)

        val textCol = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = d(12)
            }
        }
        textCol.addView(TextView(this).apply {
            text = sound.name
            setTextColor(getColor(R.color.on_surface))
            textSize = 15f
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
        })
        textCol.addView(TextView(this).apply {
            text = sound.tags
            setTextColor(getColor(R.color.on_surface_variant))
            textSize = 9f
            isAllCaps = true
            letterSpacing = 0.05f
        })
        topRow.addView(textCol)

        topRow.addView(TextView(this).apply {
            text = "${volPercent}%"
            setTextColor(getColor(R.color.on_surface_variant))
            textSize = 13f
        })

        card.addView(topRow)

        // Volume slider
        val volLabel = topRow.getChildAt(topRow.childCount - 1) as TextView
        val slider = SeekBar(this).apply {
            max = 100
            progress = volPercent
            progressTintList = android.content.res.ColorStateList.valueOf(theme.primary)
            thumbTintList = android.content.res.ColorStateList.valueOf(theme.primary)
            progressBackgroundTintList = android.content.res.ColorStateList.valueOf(
                getColor(R.color.surface_container_highest)
            )
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = d(4) }
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) {
                    if (fromUser) {
                        AudioPlayerManager.setVolume(sound.id, p / 100f)
                        volLabel.text = "${p}%"
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }
        card.addView(slider)

        return card
    }

    private fun setTimer(minutes: Int) {
        timerTotalMs = minutes * 60 * 1000L
        AudioPlayerManager.startTimer(minutes)
        binding.tvTimerDisplay.text = "${minutes}m"
        binding.timerRing.progress = 1f
    }

    private fun updateTimerDisplay() {
        if (AudioPlayerManager.hasTimer) {
            val min = AudioPlayerManager.timerRemaining / 60000
            binding.tvTimerDisplay.text = "${min}m"
        } else {
            binding.tvTimerDisplay.text = "--"
            binding.timerRing.progress = 0f
        }
    }

    private fun d(v: Int) = (v * dp).toInt()

    override fun onDestroy() {
        super.onDestroy()
        AudioPlayerManager.onProgressChanged = null
        AudioPlayerManager.onTimerTick = null
        AudioPlayerManager.onTimerFinish = null
    }
}
