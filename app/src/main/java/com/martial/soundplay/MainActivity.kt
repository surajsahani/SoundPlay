package com.martial.soundplay

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spannable
import android.text.style.StyleSpan
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
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dp by lazy { resources.displayMetrics.density }
    private var selectedMood = "Sleep"
    private val moods = listOf("Sleep", "Focus", "Relax", "Meditate")

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGreeting()
        buildMoodChips()
        showSounds(null)

        AudioPlayerManager.onStateChanged = { runOnUiThread {
            showSounds(null)
            updateMixingBar()
        }}

        applyTheme()
        setupTabs()

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.martial.soundplay.ui.SettingsActivity::class.java))
        }

        binding.featuredCard.setOnClickListener {
            AudioPlayerManager.toggleSound(this, SoundRepository.sounds[2])
            showSounds(null)
            updateMixingBar()
        }
    }

    private fun setGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val timeGreeting = when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
        val full = "$timeGreeting, Elena"
        val span = SpannableString(full)
        val nameStart = full.indexOf("Elena")
        span.setSpan(StyleSpan(Typeface.ITALIC), nameStart, full.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.greeting.text = span
    }

    private fun buildMoodChips() {
        binding.moodChips.removeAllViews()
        moods.forEach { mood ->
            val chip = TextView(this).apply {
                text = mood
                textSize = 12f
                setPadding(d(16), d(8), d(16), d(8))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = d(8) }
                if (mood == selectedMood) {
                    setBackgroundResource(R.drawable.bg_mood_chip_selected)
                    setTextColor(getColor(R.color.on_primary))
                    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
                } else {
                    setBackgroundResource(R.drawable.bg_mood_chip)
                    setTextColor(getColor(R.color.on_surface))
                }
                setOnClickListener {
                    selectedMood = mood
                    buildMoodChips()
                    showSounds(null)
                }
            }
            binding.moodChips.addView(chip)
        }
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
        showSounds(null)
        updateMixingBar()
    }

    private fun showSounds(filtered: List<Sound>?) {
        val list = binding.soundList
        list.removeAllViews()
        val sounds = filtered ?: SoundRepository.sounds
        if (sounds.isEmpty()) {
            list.addView(tv("No sounds yet.", R.color.on_surface_variant, 14f, false).apply {
                setPadding(0, d(24), 0, d(24))
            })
            return
        }
        sounds.forEach { list.addView(buildSoundRow(it)) }
    }

    private fun buildSoundRow(sound: Sound): LinearLayout {
        val isActive = AudioPlayerManager.isPlayingSound(sound.id)
        val theme = ThemeManager.get(this)

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_sound_row_card)
            setPadding(d(12), d(12), d(12), d(12))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = d(8) }
            setOnClickListener {
                AudioPlayerManager.toggleSound(this@MainActivity, sound)
                showSounds(null)
                updateMixingBar()
            }
        }

        val thumb = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(48), d(48))
            setBackgroundResource(R.drawable.bg_thumb_circle)
            if (isActive) background.setTint(theme.primaryContainer)
        }
        thumb.addView(ImageView(this).apply {
            setImageResource(sound.iconResId)
            layoutParams = FrameLayout.LayoutParams(d(22), d(22)).apply { gravity = Gravity.CENTER }
            if (isActive) setColorFilter(theme.primary)
        })
        row.addView(thumb)

        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = d(12)
            }
        }
        col.addView(tv(sound.name, R.color.on_surface, 15f, true))
        col.addView(tv(sound.category.uppercase(), R.color.on_surface_variant, 9f, false).apply {
            letterSpacing = 0.05f
        })
        row.addView(col)

        if (isActive) {
            row.addView(ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(d(18), d(18)).apply { marginEnd = d(8) }
                setImageResource(R.drawable.ic_equalizer)
            })
        }

        row.addView(ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(34), d(34))
            if (isActive) {
                setBackgroundResource(R.drawable.bg_play_button)
                background.setTint(theme.primary)
                setImageResource(R.drawable.ic_pause)
            } else {
                setBackgroundResource(R.drawable.bg_play_small)
                setImageResource(R.drawable.ic_play_filled)
            }
            setPadding(d(8), d(8), d(8), d(8))
        })

        return row
    }

    private fun applyTheme() {
        val theme = ThemeManager.get(this)
        binding.tabHome.setColorFilter(theme.primary)
        @Suppress("DEPRECATION")
        window.navigationBarColor = theme.surface
    }

    private fun updateMixingBar() {
        val playing = AudioPlayerManager.playingSounds
        if (playing.isNotEmpty()) {
            binding.mixingBar.visibility = android.view.View.VISIBLE
            binding.mixingText.text = "${playing.size} sound${if (playing.size > 1) "s" else ""} mixing"
            binding.btnOpenMixer.setOnClickListener {
                startActivity(Intent(this, PlayerActivity::class.java))
            }
            binding.btnOpenMixer.background.setTint(ThemeManager.get(this).primary)
        } else {
            binding.mixingBar.visibility = android.view.View.GONE
        }
    }

    private fun setupTabs() {
        val tabs = listOf(binding.tabHome, binding.tabSounds, binding.tabNature, binding.tabFavorites)
        fun selectTab(selected: ImageView) {
            tabs.forEach { it.setColorFilter(getColor(R.color.on_surface_variant)) }
            selected.setColorFilter(ThemeManager.get(this).primary)
        }
        binding.tabHome.setOnClickListener {
            selectTab(binding.tabHome)
            binding.sectionTitle.text = "RECOMMENDED FOR YOU"
            showSounds(null)
        }
        binding.tabSounds.setOnClickListener {
            selectTab(binding.tabSounds)
            binding.sectionTitle.text = "ALL SOUNDS"
            showSounds(null)
        }
        binding.tabNature.setOnClickListener {
            selectTab(binding.tabNature)
            binding.sectionTitle.text = "NATURE SOUNDS"
            showSounds(SoundRepository.natureSounds())
        }
        binding.tabFavorites.setOnClickListener {
            selectTab(binding.tabFavorites)
            binding.sectionTitle.text = "YOUR FAVORITES"
            showSounds(SoundRepository.favorites())
        }
    }

    private fun d(v: Int) = (v * dp).toInt()
    private fun tv(text: String, colorRes: Int, size: Float, medium: Boolean) = TextView(this).apply {
        this.text = text
        setTextColor(getColor(colorRes))
        textSize = size
        if (medium) typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }
}
