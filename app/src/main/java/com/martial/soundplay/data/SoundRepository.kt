package com.martial.soundplay.data

import com.martial.soundplay.R

object SoundRepository {
    val sounds = listOf(
        Sound(1, "Heavy Rain", "ACTIVE • 120 BPM", R.drawable.ic_rain, "nature"),
        Sound(2, "Singing Bowls", "ETHEREAL • 15 MIN", R.drawable.ic_bowls, "healing"),
        Sound(3, "Forest Whisper", "NATURE • 45 MIN", R.drawable.ic_wind, "nature"),
        Sound(4, "Deep Static", "FOCUS • INFINITE", R.drawable.ic_whitenoise, "focus"),
        Sound(5, "Campfire", "WARM • COZY", R.drawable.ic_campfire, "nature"),
        Sound(6, "Deep Ocean", "RHYTHMIC • BASS", R.drawable.ic_ocean, "nature"),
        Sound(7, "Night Birds", "CALM • AMBIENT", R.drawable.ic_nightbirds, "nature")
    )

    fun natureSounds() = sounds.filter { it.category == "nature" }
    fun favorites() = sounds.filter { it.isFavorite }
}
