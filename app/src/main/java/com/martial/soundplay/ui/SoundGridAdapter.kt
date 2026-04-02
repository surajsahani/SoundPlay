package com.martial.soundplay.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martial.soundplay.data.Sound
import com.martial.soundplay.databinding.ItemSoundCardBinding

class SoundGridAdapter(
    private val sounds: List<Sound>,
    private val onClick: (Sound) -> Unit
) : RecyclerView.Adapter<SoundGridAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSoundCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sound: Sound) {
            binding.soundIcon.setImageResource(sound.iconResId)
            binding.soundName.text = sound.name
            binding.soundTags.text = sound.tags
            binding.root.setOnClickListener { onClick(sound) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoundCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(sounds[position])
    override fun getItemCount() = sounds.size
}
