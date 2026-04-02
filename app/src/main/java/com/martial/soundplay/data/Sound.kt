package com.martial.soundplay.data

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Sound(
    val id: Int,
    val name: String,
    val category: String,
    val tags: String,
    @RawRes val rawResId: Int,
    @DrawableRes val iconResId: Int,
    val durationSeconds: Int = 0,
    var isBookmarked: Boolean = false
)
