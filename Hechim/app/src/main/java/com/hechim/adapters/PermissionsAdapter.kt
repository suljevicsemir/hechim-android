package com.hechim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hechim.databinding.ItemViewPagerBinding
import com.hechim.databinding.PermissionItemBinding
import com.hechim.models.local.AppPermission
import com.hechim.models.local.OnBoardingItem

class PermissionsAdapter(
    private val items: List<AppPermission>
): RecyclerView.Adapter<PermissionsAdapter.PermissionViewHolder>() {

    inner class PermissionViewHolder(
        val binding: PermissionItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PermissionItemBinding.inflate(inflater, parent, false)
        return PermissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        val permission : AppPermission = items[position]
        holder.binding.apply {
            permissionImage.setImageResource(permission.image)
            permissionTitle.text = permissionTitle.context.getString(permission.title)
            permissionDescription.text = permissionDescription.context.getString(permission.description)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}