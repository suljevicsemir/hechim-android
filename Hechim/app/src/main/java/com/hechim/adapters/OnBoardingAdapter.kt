package com.hechim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hechim.databinding.ItemViewPagerBinding
import com.hechim.models.local.OnBoardingItem

class OnBoardingAdapter(
    private val items: List<OnBoardingItem>
): RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    inner class OnBoardingViewHolder(
        val binding: ItemViewPagerBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewPagerBinding.inflate(inflater, parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.binding.apply {
            viewPagerImage.setImageResource(items[position].image)
            viewPagerTitle.text = items[position].title
            viewPagerDescription.text = items[position].description
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}