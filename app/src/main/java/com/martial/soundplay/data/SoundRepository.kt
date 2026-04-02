package com.martial.soundplay.data

import com.martial.soundplay.R

object SoundRepository {
    val sounds = listOf(
        Sound(1, "Heavy Rain", "Nature", "NATURE • DENSE", R.raw.heavy_rain, R.drawable.ic_rain, 180),
        Sound(2, "Singing Bowls", "Healing", "HEALING • ZEN", R.raw.singing_bowls, R.drawable.ic_bowls, 210),
        Sound(3, "Forest Wind", "Nature", "NATURE • AIRY", R.raw.forest_wind, R.drawable.ic_wind, 195),
        Sound(4, "Deep Ocean", "Nature", "RHYTHMIC • BASS", R.raw.deep_ocean, R.drawable.ic_ocean, 240),
        Sound(5, "Campfire", "Nature", "WARM • COZY", R.raw.campfire, R.drawable.ic_campfire, 200),
        Sound(6, "White Noise", "Focus", "FOCUS • STATIC", R.raw.white_noise, R.drawable.ic_whitenoise, 300),
        Sound(7, "Night Birds", "Nature", "CALM • AMBIENT", R.raw.night_birds, R.drawable.ic_nightbirds, 220)
    )

    fun getByCategory(category: String): List<Sound> =
        sounds.filter { it.category.equals(category, ignoreCase = true) }

    fun getById(id: Int): Sound? = sounds.find { it.id == id }

    val categories = listOf("All", "Nature", "Healing", "Focus")
}
