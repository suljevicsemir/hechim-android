package com.hechim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hechim.R
import com.hechim.databinding.LanguageSelectionItemBinding
import com.hechim.models.local.AppLanguageItem

class LanguageSelectionAdapter(
    var items: List<AppLanguageItem>,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<LanguageSelectionAdapter.LanguageSelectionViewHolder>() {
    inner class LanguageSelectionViewHolder(
        val binding: LanguageSelectionItemBinding
    ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageSelectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LanguageSelectionItemBinding.inflate(inflater, parent, false)
        return LanguageSelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageSelectionViewHolder, position: Int) {
        val item: AppLanguageItem = items[position]
        val selectedImage = if(item.selected) R.drawable.ic_radio_on else R.drawable.ic_radio_off
        holder.binding.apply {
            languageText.setText(item.text)
            languageItemImage.setImageResource(item.image)
            languageSelectedImage.setImageResource(selectedImage)
        }
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ItemClickListener {
        fun onItemClick(item: AppLanguageItem): Unit
    }
}