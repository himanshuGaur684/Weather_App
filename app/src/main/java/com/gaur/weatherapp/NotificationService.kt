package com.gaur.weatherapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.gaur.weatherapp.repository.CurrentConditionRepo
import com.gaur.weatherapp.utils.AppSharedPref
import com.gaur.weatherapp.utils.NotificationChannelConstant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : Service() {


    @Inject
    lateinit var currentConditionRepo: CurrentConditionRepo

    @Inject
    lateinit var appPref: AppSharedPref
    private var notificationManagerCompat: NotificationManagerCompat? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = currentConditionRepo.getCurrentLocationWeather(
                    appPref.getLatitude().toDouble(),
                    appPref.getLongitude().toDouble()
                )
                val body = response.body()
                val title = "Location: ${body?.name} Temp: ${body?.main?.temp?.minus(273)}"
                val contentImageUrl =
                    "https://openweathermap.org/img/wn/${body?.weather?.get(0)?.icon}@2x.png"
                val bitmap =
                    Glide.with(this@NotificationService).asBitmap().load(contentImageUrl).submit()
                        .get()
                notificationManagerCompat = NotificationManagerCompat.from(this@NotificationService)
                val notification = NotificationCompat.Builder(
                    this@NotificationService,
                    NotificationChannelConstant.NOTIFICATION_CHANNEL_1
                )
                    .setContentTitle("Current Weather Info")
                    .setContentText(title)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.ic_circle)
                    .build()
                notificationManagerCompat?.notify(0, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return START_STICKY
    }


}