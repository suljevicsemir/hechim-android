package com.hechim.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.hechim.MainActivity
import com.hechim.R
import java.util.*
import kotlin.time.Duration.Companion.seconds


class TestForeground: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val builder = NotificationCompat.Builder(this, "RideArrival")
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
            .addAction(R.drawable.ic_not_started, "s", null)
            .addAction(R.drawable.ic_pause, "s", null)
            .setContentText("Notification description")
            .setContentTitle("Notification title")
            .setColor(ContextCompat.getColor(this, R.color.orange_400))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE))



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startForeground(1, builder.build())
        startLocationTracking(builder, notificationManager)
        return START_NOT_STICKY
    }

    fun formatDuration(seconds: Long): String = DateUtils.formatElapsedTime(seconds)

    fun formatDurationTime(durationSeconds: Long) =
        durationSeconds.seconds.toComponents { hours, minutes, seconds, _ ->
            String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds,
            )
        }

    @SuppressLint("MissingPermission")
    fun startLocationTracking(builder: NotificationCompat.Builder, notificationManager: NotificationManager) {
        val mGpsLocationClient: LocationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        var initialPosition = Location("")
        var distance = 0f
        var time = 0L

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                time++
                builder.setContentText(formatDurationTime(time))
                notificationManager.notify(1, builder.build())
                mainHandler.postDelayed(this, 1000)
            }
        })

        mGpsLocationClient.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        1000,
        10.0f
        ) {
            if(initialPosition.provider!!.isEmpty()) {
                initialPosition.latitude = it.latitude
                initialPosition.longitude = it.longitude
                initialPosition.speed = it.speed
                initialPosition.altitude = it.altitude
                initialPosition.provider = "updated"
            }
            else {
                distance += initialPosition.distanceTo(it)

                initialPosition = it
            }
            builder.setContentText("%.2f".format(distance / 1000))
            val titleContent = "%.2f".format(distance / 1000) + " km"

            builder.setContentTitle(HtmlCompat.fromHtml(
                "<font color=\"" +
                        ContextCompat.getColor(this, R.color.orange_400) +
                        "\">" + titleContent + "</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY))
            time++
            notificationManager.notify(1, builder.build())
        }
    }

    private val locationListener = android.location.LocationListener { location -> //handle location change
            println(location)
        }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}