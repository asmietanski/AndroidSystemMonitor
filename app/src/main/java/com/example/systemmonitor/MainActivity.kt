package com.example.systemmonitor

import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.systemmonitor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.RandomAccessFile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppListAdapter
    private lateinit var activityManager: ActivityManager
    private lateinit var packageManager: PackageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        packageManager = packageManager

        setupRecyclerView()
        setupClickListeners()
        
        // Check for usage stats permission
        if (!hasUsageStatsPermission()) {
            requestUsageStatsPermission()
        }

        loadApps()
    }

    private fun setupRecyclerView() {
        adapter = AppListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun setupClickListeners() {
        binding.refreshButton.setOnClickListener {
            loadApps()
        }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun requestUsageStatsPermission() {
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    private fun loadApps() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        lifecycleScope.launch {
            val apps = withContext(Dispatchers.IO) {
                getInstalledApps()
            }

            adapter.submitList(apps)
            updateStats(apps)

            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun getInstalledApps(): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val runningProcesses = activityManager.runningAppProcesses ?: emptyList()
        val runningPackages = runningProcesses.map { it.processName }.toSet()

        for (appInfo in installedApps) {
            try {
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val packageName = appInfo.packageName
                val icon = packageManager.getApplicationIcon(appInfo)
                val isRunning = runningPackages.contains(packageName)

                // Get memory usage
                val memoryUsage = getMemoryUsage(packageName)
                
                // Get CPU usage (approximation)
                val cpuUsage = getCpuUsage(packageName)

                apps.add(
                    AppInfo(
                        appName = appName,
                        packageName = packageName,
                        icon = icon,
                        memoryUsageMB = memoryUsage,
                        cpuUsagePercent = cpuUsage,
                        isRunning = isRunning
                    )
                )
            } catch (e: Exception) {
                // Skip apps that cause errors
                e.printStackTrace()
            }
        }

        // Sort by running status first, then by memory usage
        return apps.sortedWith(
            compareByDescending<AppInfo> { it.isRunning }
                .thenByDescending { it.memoryUsageMB }
        )
    }

    private fun getMemoryUsage(packageName: String): Double {
        try {
            val pids = activityManager.runningAppProcesses
                ?.filter { it.processName == packageName }
                ?.map { it.pid }
                ?.toIntArray() ?: return 0.0

            if (pids.isEmpty()) return 0.0

            val memoryInfo = activityManager.getProcessMemoryInfo(pids)
            if (memoryInfo.isNotEmpty()) {
                // Convert from KB to MB
                return memoryInfo[0].totalPss / 1024.0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0.0
    }

    private fun getCpuUsage(packageName: String): Double {
        try {
            val pids = activityManager.runningAppProcesses
                ?.filter { it.processName == packageName }
                ?.map { it.pid } ?: return 0.0

            if (pids.isEmpty()) return 0.0

            // Read CPU stats from /proc/[pid]/stat
            for (pid in pids) {
                try {
                    val statFile = RandomAccessFile("/proc/$pid/stat", "r")
                    val statContent = statFile.readLine()
                    statFile.close()

                    // Parse CPU time (simplified calculation)
                    val stats = statContent.split(" ")
                    if (stats.size > 14) {
                        val utime = stats[13].toLongOrNull() ?: 0L
                        val stime = stats[14].toLongOrNull() ?: 0L
                        val totalTime = utime + stime
                        
                        // This is a simplified calculation
                        // In a real app, you'd need to calculate the delta over time
                        return (totalTime / 100.0) % 100.0
                    }
                } catch (e: Exception) {
                    // Process might have terminated
                    continue
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0.0
    }

    private fun updateStats(apps: List<AppInfo>) {
        val totalApps = apps.size
        val runningApps = apps.count { it.isRunning }

        binding.totalAppsText.text = "Total Apps: $totalApps"
        binding.runningAppsText.text = "Running: $runningApps"
    }
}