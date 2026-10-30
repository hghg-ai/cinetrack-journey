package com.binh.cinetrack.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.binh.cinetrack.MainActivity
import com.binh.cinetrack.R
import com.binh.cinetrack.domain.MovieRepository
import com.binh.cinetrack.domain.top5ByRating
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class DailyTrendingWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workParams: WorkerParameters,
    private val repository: MovieRepository
) : CoroutineWorker(context, workParams) {
    companion object{
        const val CHANNEL_ID = "daily_trending_channel"
        const val CHANNEL_NAME = "Phim đề xuất"
        const val NOTIFICATION_ID = 1001
    }
    override suspend fun doWork(): Result{
        return try {
            val movies = repository.getTrendingMovies().firstOrNull() ?: repository.getMovies()
            if (movies.isEmpty()) return Result.success()

            val topTitles =  movies.top5ByRating().take(2).joinToString(", "){it.title}
            showNotification(topTitles)
            Result.success()
        }catch (e: Exception){
            Result.retry()
        }
    }
    private fun showNotification(movieTitles: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Tạo Notification Channel (Yêu cầu bắt buộc từ Android 8.0 / API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Kênh thông báo phim hot mỗi ngày"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo PendingIntent để khi bấm vào Notification sẽ mở MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,0,intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Xây dựng giao diện Notìication
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Hôm nay xem gì")
            .setContentText(movieTitles)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Hôm nay có gì hot: $movieTitles"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}