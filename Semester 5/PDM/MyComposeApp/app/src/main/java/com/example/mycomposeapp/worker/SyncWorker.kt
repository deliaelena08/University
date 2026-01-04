package com.example.mycomposeapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mycomposeapp.data.api.RetrofitClient
import com.example.mycomposeapp.data.database.AppDatabase
import com.example.mycomposeapp.data.repository.PostRepository
import com.example.mycomposeapp.data.repository.TokenManager
import kotlinx.coroutines.flow.first

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("SyncWorker", "--- STARTED WORKER ---")
        showToast("Sync Started...")

        return try {
            val database = AppDatabase.getDatabase(applicationContext)
            val tokenManager = TokenManager(applicationContext)
            val token = tokenManager.token.first()

            if (token == null) {
                showToast("Sync Failed: No Token")
                return Result.failure()
            }

            val apiService = RetrofitClient.create { token }
            val repository = PostRepository(apiService, database.postDao())

            val unsyncedPosts = database.postDao().getUnsyncedPosts()
            var uploadedCount = 0

            for (localPost in unsyncedPosts) {
                if (localPost.id > 0) {
                    Log.d("SyncWorker", "Skipping post ID ${localPost.id} because it is already on server.")
                    continue
                }

                Log.d("SyncWorker", "Uploading post ID: ${localPost.id}")
                try {
                    val response = apiService.createPost(
                        localPost.description ?: "",
                        localPost.photoUrl,
                        localPost.location
                    )

                    if (response.isSuccessful) {
                        database.postDao().deleteById(localPost.id)
                        uploadedCount++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val refreshResult = repository.refreshPosts()

            if (uploadedCount > 0) {
                val msg = "Uploaded $uploadedCount offline posts!"
                sendNotification("Sync Complete", msg)
                showToast(msg)
            } else if (refreshResult.isSuccess) {
                showToast("Sync Finished: Data updated")
            } else {
                showToast("Sync Finished with errors")
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "sync_channel_high_priority"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Sync Critical", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {}
    }
}