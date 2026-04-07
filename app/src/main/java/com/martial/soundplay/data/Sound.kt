package com.martial.soundplay.data

import androidx.annotation.DrawableRes

data class Sound(
    val id: Int,
    val name: String,
    val tags: String,
    @DrawableRes val iconResId: Int,
    val category: String = "general",
    var isFavorite: Boolean = false
)
