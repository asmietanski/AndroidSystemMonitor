package com.example.systemmonitor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.systemmonitor.databinding.ItemAppBinding

/**
 * RecyclerView adapter for displaying app information
 */
class AppListAdapter : ListAdapter<AppInfo, AppListAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppViewHolder(private val binding: ItemAppBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(appInfo: AppInfo) {
            binding.apply {
                appIcon.setImageDrawable(appInfo.icon)
                appName.text = appInfo.appName
                packageName.text = appInfo.packageName
                memoryUsage.text = "Memory: ${appInfo.getFormattedMemory()}"
                cpuUsage.text = "CPU: ${appInfo.getFormattedCpu()}"
                
                // Show running indicator
                val statusText = if (appInfo.isRunning) "Running" else "Stopped"
                runningStatus.text = statusText
                runningStatus.setTextColor(
                    if (appInfo.isRunning) 
                        binding.root.context.getColor(android.R.color.holo_green_dark)
                    else 
                        binding.root.context.getColor(android.R.color.darker_gray)
                )
            }
        }
    }

    private class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
}