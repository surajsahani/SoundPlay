package com.martial.soundplay

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.martial.soundplay.audio.AudioPlayerManager
import com.martial.soundplay.data.Sound
import com.martial.soundplay.data.SoundRepository
import com.martial.soundplay.databinding.ActivityMainBinding
import com.martial.soundplay.ui.PlayerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dp by lazy { resources.displayMetrics.density }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buildSoundList()

        binding.featuredCard.setOnClickListener {
            AudioPlayerManager.play(SoundRepository.sounds[1]) // Singing Bowls
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }

    private fun buildSoundList() {
        val list = binding.soundList
        SoundRepository.sounds.forEachIndexed { i, sound ->
            list.addView(if (i == 0) buildFirstItem(sound) else buildSoundRow(sound))
        }
    }

    private fun buildFirstItem(sound: Sound): LinearLayout {
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_first_sound)
            setPadding(d(16), d(16), d(12), d(16))
            layoutParams = lp().apply { bottomMargin = d(14) }
            setOnClickListener { launch(sound) }
        }

        // Large icon circle
        val iconFrame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(72), d(72))
            setBackgroundResource(R.drawable.bg_icon_circle)
        }
        iconFrame.addView(ImageView(this).apply {
            setImageResource(sound.iconResId)
            layoutParams = FrameLayout.LayoutParams(d(28), d(28)).apply { gravity = Gravity.CENTER }
        })
        outer.addView(iconFrame)

        // Text
        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = d(14)
            }
        }
        col.addView(tv(sound.name, R.color.on_surface, 16f, true))
        col.addView(tv(sound.tags, R.color.on_surface_variant, 9f, false).apply {
            isAllCaps = true; letterSpacing = 0.05f; setPadding(0, d(3), 0, 0)
        })
        outer.addView(col)

        // Teal play button
        outer.addView(ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(44), d(44))
            setBackgroundResource(R.drawable.bg_play_button)
            setImageResource(R.drawable.ic_pause)
            setPadding(d(11), d(11), d(11), d(11))
        })

        return outer
    }

    private fun buildSoundRow(sound: Sound): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_sound_row)
            setPadding(d(5), d(5), d(5), d(5))
            layoutParams = lp().apply { bottomMargin = d(10) }
            setOnClickListener { launch(sound) }
        }

        // Icon circle
        val iconFrame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(48), d(48))
            setBackgroundResource(R.drawable.bg_icon_circle)
        }
        iconFrame.addView(ImageView(this).apply {
            setImageResource(sound.iconResId)
            layoutParams = FrameLayout.LayoutParams(d(22), d(22)).apply { gravity = Gravity.CENTER }
        })
        row.addView(iconFrame)

        // Text
        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = d(12)
            }
        }
        col.addView(tv(sound.name, R.color.on_surface, 14f, true))
        col.addView(tv(sound.tags, R.color.on_surface_variant, 9f, false).apply {
            isAllCaps = true; letterSpacing = 0.05f
        })
        row.addView(col)

        // Small play circle
        row.addView(ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(34), d(34)).apply { marginEnd = d(4) }
            setBackgroundResource(R.drawable.bg_play_small)
            setImageResource(R.drawable.ic_play_filled)
            setPadding(d(9), d(9), d(9), d(9))
        })

        return row
    }

    private fun launch(sound: Sound) {
        AudioPlayerManager.play(sound)
        startActivity(Intent(this, PlayerActivity::class.java))
    }

    private fun d(v: Int) = (v * dp).toInt()
    private fun lp() = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )
    private fun tv(text: String, colorRes: Int, size: Float, medium: Boolean) = TextView(this).apply {
        this.text = text
        setTextColor(getColor(colorRes))
        textSize = size
        if (medium) typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
    }
}
