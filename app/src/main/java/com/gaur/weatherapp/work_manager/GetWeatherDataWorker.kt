package com.gaur.weatherapp.work_manager

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gaur.weatherapp.NotificationService


class GetWeatherDataWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,

    ) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val intent = Intent(context, NotificationService::class.java)
        context.startService(intent)
        return Result.retry()
    }
}