package com.martial.soundplay.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martial.soundplay.R
import com.martial.soundplay.data.Sound
import com.martial.soundplay.databinding.ItemLibrarySoundBinding

class LibrarySoundAdapter(
    private var sounds: List<Sound>,
    private val onClick: (Sound) -> Unit
) : RecyclerView.Adapter<LibrarySoundAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLibrarySoundBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sound: Sound) {
            binding.libIcon.setImageResource(sound.iconResId)
            binding.libName.text = sound.name
            binding.libTags.text = sound.tags
            binding.libBookmark.setColorFilter(
                if (sound.isBookmarked) binding.root.context.getColor(R.color.primary)
                else binding.root.context.getColor(R.color.outline_variant)
            )
            binding.libBookmark.setOnClickListener {
                sound.isBookmarked = !sound.isBookmarked
                notifyItemChanged(adapterPosition)
            }
            binding.root.setOnClickListener { onClick(sound) }
        }
    }

    fun updateList(newSounds: List<Sound>) {
        sounds = newSounds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLibrarySoundBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(sounds[position])
    override fun getItemCount() = sounds.size
}
