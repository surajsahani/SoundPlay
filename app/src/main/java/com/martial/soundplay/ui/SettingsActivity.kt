package com.martial.soundplay.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.martial.soundplay.R
import com.martial.soundplay.ThemeManager
import com.martial.soundplay.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val dp by lazy { resources.displayMetrics.density }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.tvVersion.text = packageManager.getPackageInfo(packageName, 0).versionName

        buildThemeList()
    }

    private fun buildThemeList() {
        val current = ThemeManager.getIndex(this)

        ThemeManager.themes.forEachIndexed { index, theme ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setBackgroundResource(R.drawable.bg_sound_row)
                setPadding(d(12), d(12), d(12), d(12))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = d(10) }
                setOnClickListener {
                    ThemeManager.save(this@SettingsActivity, index)
                    recreate()
                }
            }

            // Color circle
            val circle = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(d(36), d(36))
                background = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(theme.primary)
                }
            }
            row.addView(circle)

            // Theme name
            row.addView(TextView(this).apply {
                text = theme.name
                setTextColor(getColor(R.color.on_surface))
                textSize = 15f
                typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginStart = d(14)
                }
            })

            // Checkmark if selected
            if (index == current) {
                row.addView(TextView(this).apply {
                    text = "✓"
                    setTextColor(theme.primary)
                    textSize = 18f
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { marginEnd = d(8) }
                })
            }

            binding.themeList.addView(row)
        }
    }

    private fun d(v: Int) = (v * dp).toInt()
}
