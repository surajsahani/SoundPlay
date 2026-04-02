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
            AudioPlayerManager.play(SoundRepository.sounds.first())
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }

    private fun buildSoundList() {
        val list = binding.soundList
        val sounds = SoundRepository.sounds

        sounds.forEachIndexed { index, sound ->
            if (index == 0) {
                list.addView(buildFirstItem(sound))
            } else {
                list.addView(buildSoundRow(sound))
            }
        }
    }

    // First item — bigger card with icon, name, tags, and play button
    private fun buildFirstItem(sound: Sound): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_first_sound)
            setPadding((20 * dp).toInt(), (20 * dp).toInt(), (16 * dp).toInt(), (20 * dp).toInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = (12 * dp).toInt() }
            setOnClickListener { launchPlayer(sound) }
        }

        // Icon circle
        val iconFrame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams((64 * dp).toInt(), (64 * dp).toInt())
            setBackgroundResource(R.drawable.bg_icon_circle)
        }
        iconFrame.addView(ImageView(this).apply {
            setImageResource(sound.iconResId)
            layoutParams = FrameLayout.LayoutParams((28 * dp).toInt(), (28 * dp).toInt()).apply {
                gravity = Gravity.CENTER
            }
        })
        card.addView(iconFrame)

        // Text
        val textCol = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = (16 * dp).toInt()
            }
        }
        textCol.addView(TextView(this).apply {
            text = sound.name
            setTextColor(getColor(R.color.on_surface))
            textSize = 17f
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
        })
        textCol.addView(TextView(this).apply {
            text = sound.tags
            setTextColor(getColor(R.color.on_surface_variant))
            textSize = 10f
            isAllCaps = true
            letterSpacing = 0.05f
            setPadding(0, (3 * dp).toInt(), 0, 0)
        })
        card.addView(textCol)

        // Play button
        card.addView(ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams((44 * dp).toInt(), (44 * dp).toInt())
            setBackgroundResource(R.drawable.bg_play_button)
            setImageResource(R.drawable.ic_pause)
            val pad = (10 * dp).toInt()
            setPadding(pad, pad, pad, pad)
        })

        return card
    }

    // Regular sound row — pill shaped
    private fun buildSoundRow(sound: Sound): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_sound_row)
            setPadding((6 * dp).toInt(), (6 * dp).toInt(), (6 * dp).toInt(), (6 * dp).toInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = (10 * dp).toInt() }
            setOnClickListener { launchPlayer(sound) }
        }

        // Icon circle
        val iconFrame = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams((44 * dp).toInt(), (44 * dp).toInt())
            setBackgroundResource(R.drawable.bg_icon_circle)
        }
        iconFrame.addView(ImageView(this).apply {
            setImageResource(sound.iconResId)
            layoutParams = FrameLayout.LayoutParams((20 * dp).toInt(), (20 * dp).toInt()).apply {
                gravity = Gravity.CENTER
            }
        })
        row.addView(iconFrame)

        // Text
        val textCol = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = (12 * dp).toInt()
            }
        }
        textCol.addView(TextView(this).apply {
            text = sound.name
            setTextColor(getColor(R.color.on_surface))
            textSize = 14f
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
        })
        textCol.addView(TextView(this).apply {
            text = sound.tags
            setTextColor(getColor(R.color.on_surface_variant))
            textSize = 9f
            isAllCaps = true
            letterSpacing = 0.05f
        })
        row.addView(textCol)

        // Small play button
        row.addView(ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams((32 * dp).toInt(), (32 * dp).toInt()).apply {
                marginEnd = (4 * dp).toInt()
            }
            setBackgroundResource(R.drawable.bg_play_small)
            setImageResource(R.drawable.ic_play)
            val pad = (8 * dp).toInt()
            setPadding(pad, pad, pad, pad)
        })

        return row
    }

    private fun launchPlayer(sound: Sound) {
        AudioPlayerManager.play(sound)
        startActivity(Intent(this, PlayerActivity::class.java))
    }
}
