package com.martial.soundplay

import android.content.Context
import android.content.SharedPreferences

object ThemeManager {
    private const val PREF_NAME = "aether_prefs"
    private const val KEY_THEME = "theme"

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

    fun get(context: Context): AppTheme = themes[getIndex(context)]

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}
