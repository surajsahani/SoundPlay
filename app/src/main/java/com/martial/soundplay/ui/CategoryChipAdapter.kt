package com.martial.soundplay.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martial.soundplay.R
import com.martial.soundplay.databinding.ItemCategoryChipBinding

class CategoryChipAdapter(
    private val categories: List<String>,
    private val onSelect: (String) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.ViewHolder>() {

    private var selectedIndex = 0

    inner class ViewHolder(val binding: ItemCategoryChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String, position: Int) {
            binding.chipText.text = category
            binding.chipText.isSelected = position == selectedIndex
            binding.chipText.setTextColor(
                if (position == selectedIndex)
                    binding.root.context.getColor(R.color.on_primary)
                else
                    binding.root.context.getColor(R.color.on_surface)
            )
            binding.chipText.setOnClickListener {
                val prev = selectedIndex
                selectedIndex = position
                notifyItemChanged(prev)
                notifyItemChanged(position)
                onSelect(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryChipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(categories[position], position)

    override fun getItemCount() = categories.size
}
