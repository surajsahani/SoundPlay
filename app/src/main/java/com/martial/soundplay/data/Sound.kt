package com.martial.soundplay.data

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Sound(
    val id: Int,
    val name: String,
    val tags: String,
    @DrawableRes val iconResId: Int,
    @RawRes val rawResId: Int,
    val category: String = "general",
    var isFavorite: Boolean = false
)
