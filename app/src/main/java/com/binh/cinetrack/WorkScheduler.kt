package com.binh.cinetrack

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.binh.cinetrack.data.worker.DailyTrendingWorker
import java.util.concurrent.TimeUnit

object WorkScheduler {
    private const val WORK_NAME = "daily_trending_work"
    //hàm đặt lịch nhắc phim hàng ngày
    fun scheduleDailyNotification(context: Context, isTesting: Boolean = true){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workManager = WorkManager.getInstance(context)

        val periodicWorkRequest = PeriodicWorkRequestBuilder<DailyTrendingWorker>(3, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

// Hàm hủy lịch nhắc phim
fun cancelDailyNotification(context: Context){
    WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}