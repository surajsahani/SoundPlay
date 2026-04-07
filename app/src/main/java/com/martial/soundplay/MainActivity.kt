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

        AudioPlayerManager.onStateChanged = { runOnUiThread { updateNowPlaying() } }

        applyTheme()

        setupTabs()

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, com.martial.soundplay.ui.SettingsActivity::class.java))
        }

        binding.featuredCard.setOnClickListener {
            AudioPlayerManager.play(this, SoundRepository.sounds[1])
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }

    private fun buildSoundList() {
        val list = binding.soundList
        SoundRepository.sounds.forEach { sound ->
            list.addView(buildSoundRow(sound))
        }
    }

    override fun onResume() {
        super.onResume()
        applyTheme()
        updateNowPlaying()
    }

    private fun updateNowPlaying() {
        showSounds(null)
    }

    private fun buildFirstItem(sound: Sound): FrameLayout {
        // Outer glow container
        val wrapper = FrameLayout(this).apply {
            setBackgroundResource(R.drawable.bg_first_sound_outer)
            setPadding(d(10), d(10), d(10), d(10))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = d(14) }
        }

        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundResource(R.drawable.bg_first_sound)
            setPadding(d(16), d(16), d(12), d(16))
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
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
        // Teal play button — use theme color
        val playImg = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(d(44), d(44))
            setBackgroundResource(R.drawable.bg_play_button)
            background.setTint(ThemeManager.get(this@MainActivity).primary)
            setImageResource(R.drawable.ic_pause)
            setPadding(d(11), d(11), d(11), d(11))
        }
        outer.addView(playImg)

        wrapper.addView(outer)
        return wrapper
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
        AudioPlayerManager.play(this, sound)
        startActivity(Intent(this, PlayerActivity::class.java))
    }

    private fun applyTheme() {
        val theme = ThemeManager.get(this)
        binding.tabHome.setColorFilter(theme.primary)
        window.navigationBarColor = theme.surface
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

    private fun showSounds(filtered: List<Sound>?) {
        val list = binding.soundList
        list.removeAllViews()
        val sounds = filtered ?: SoundRepository.sounds
        val playing = AudioPlayerManager.currentSound

        if (sounds.isEmpty()) {
            list.addView(tv("No sounds yet.", R.color.on_surface_variant, 14f, false).apply {
                setPadding(0, d(24), 0, d(24))
            })
            return
        }

        if (playing != null && sounds.any { it.id == playing.id }) {
            list.addView(buildFirstItem(playing))
            sounds.filter { it.id != playing.id }.forEach { list.addView(buildSoundRow(it)) }
        } else {
            sounds.forEachIndexed { i, s ->
                list.addView(if (i == 0) buildFirstItem(s) else buildSoundRow(s))
            }
        }
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
