package com.martial.soundplay

import android.content.Context
import android.content.SharedPreferences

object ThemeManager {
    private const val PREF_NAME = "aether_prefs"
    private const val KEY_THEME = "theme"
    private const val KEY_DARK_MODE = "dark_mode"

    data class AppTheme(
        val name: String,
        val primary: Int,
        val primaryContainer: Int,
        val surfaceLow: Int,
        val surface: Int
    )

    val themes = listOf(
        AppTheme("Teal Mist", 0xFF276869.toInt(), 0xFFAFEEED.toInt(), 0xFFF0F4F4.toInt(), 0xFFF8FAFA.toInt()),
        AppTheme("Lavender", 0xFF5B4A8A.toInt(), 0xFFD8CCFF.toInt(), 0xFFF3F0FA.toInt(), 0xFFFAF8FF.toInt()),
        AppTheme("Rose", 0xFF8A4A5B.toInt(), 0xFFFFCCD8.toInt(), 0xFFFAF0F3.toInt(), 0xFFFFF8FA.toInt()),
        AppTheme("Forest", 0xFF3A6B3A.toInt(), 0xFFB8E6B8.toInt(), 0xFFF0F5F0.toInt(), 0xFFF8FAF8.toInt()),
        AppTheme("Amber", 0xFF7A5A2E.toInt(), 0xFFFFE0A8.toInt(), 0xFFFAF5F0.toInt(), 0xFFFFFAF5.toInt())
    )

    fun save(context: Context, index: Int) {
        prefs(context).edit().putInt(KEY_THEME, index).apply()
    }

    fun getIndex(context: Context): Int = prefs(context).getInt(KEY_THEME, 0)

    fun isDarkMode(context: Context): Boolean = prefs(context).getBoolean(KEY_DARK_MODE, false)

    fun setDarkMode(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun get(context: Context): AppTheme {
        val index = getIndex(context)
        val isDark = isDarkMode(context)
        val theme = themes[index]
        return if (isDark) {
            when (index) {
                0 -> AppTheme("Teal Mist", 0xFF4DB6AC.toInt(), 0xFF1F4D4E.toInt(), 0xFF142728.toInt(), 0xFF0B1516.toInt())
                1 -> AppTheme("Lavender", 0xFFB39DDB.toInt(), 0xFF3C3263.toInt(), 0xFF181324.toInt(), 0xFF110D1A.toInt())
                2 -> AppTheme("Rose", 0xFFF48FB1.toInt(), 0xFF602A36.toInt(), 0xFF251419.toInt(), 0xFF1A0D10.toInt())
                3 -> AppTheme("Forest", 0xFF81C784.toInt(), 0xFF254B25.toInt(), 0xFF132013.toInt(), 0xFF0D170D.toInt())
                4 -> AppTheme("Amber", 0xFFFFB74D.toInt(), 0xFF4E371C.toInt(), 0xFF21180F.toInt(), 0xFF160F0A.toInt())
                else -> theme
            }
        } else {
            theme
        }
    }

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}
