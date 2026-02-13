package com.example.systemmonitor

import android.graphics.drawable.Drawable

/**
 * Data class to hold information about an application
 */
data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable?,
    val memoryUsageMB: Double,
    val cpuUsagePercent: Double,
    val isRunning: Boolean
) {
    fun getFormattedMemory(): String {
        return String.format("%.2f MB", memoryUsageMB)
    }
    
    fun getFormattedCpu(): String {
        return String.format("%.1f%%", cpuUsagePercent)
    }
}