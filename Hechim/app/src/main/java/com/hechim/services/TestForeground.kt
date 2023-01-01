package com.hechim.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.hechim.MainActivity
import com.hechim.R
import com.hechim.models.local.WorkoutNotificationSettings
import java.util.*
import kotlin.time.Duration.Companion.seconds


class TestForeground: Service(), SensorEventListener {

    private val STOP_WORKOUT = "STOP_WORKOUT"
    private val PAUSE_WORKOUT = "PAUSE_WORKOUT"
    private val RESUME_WORKOUT = "RESUME_WORKOUT"

    private val settings = WorkoutNotificationSettings(
        paused = false,
        time = 0,
        distance = 0f,
        totalSteps = -1,
        workoutSteps = 0
    )


    private var sensorManager: SensorManager? = null




    private var builder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val stopIntent = Intent(this, TestForeground::class.java)
        stopIntent.action = STOP_WORKOUT
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, TestForeground::class.java)
        pauseIntent.action = PAUSE_WORKOUT
        val pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val resumeIntent = Intent(this, TestForeground::class.java)
        resumeIntent.action = RESUME_WORKOUT
        val pendingResumeIntent = PendingIntent.getService(this, 0, resumeIntent, PendingIntent.FLAG_IMMUTABLE)

        //check if intent action should stop the workout
        if(intentActionEquals(STOP_WORKOUT, intent)) {
            return stopWorkout()
        }
        //check if intent action should pause the workout
        else if(intentActionEquals(PAUSE_WORKOUT, intent)) {
            return pauseWorkout(
                pendingStopIntent = pendingStopIntent,
                pendingResumeIntent = pendingResumeIntent
            )
        }
        //check if intent action should resume the workout
        else if(intentActionEquals(RESUME_WORKOUT, intent)) {
            return resumeWorkout(
                pendingStopIntent = pendingStopIntent,
                pendingPauseIntent = pendingPauseIntent
            )
        }
        //start workout
        else {
            val notificationIntent = Intent(this, MainActivity::class.java)


            builder = NotificationCompat.Builder(this, "RideArrival")
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
                .addAction(R.drawable.ic_pause, "Pause", pendingPauseIntent)
                .addAction(R.drawable.ic_stop, "Stop", pendingStopIntent)
                .setColor(ContextCompat.getColor(this, R.color.orange_400))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE))


            startForeground(1, builder!!.build())
            startLocationTracking()
            setupSensors()
            setupTimer()

            return START_NOT_STICKY
        }




    }

    private fun intentActionEquals(action: String, intent: Intent?):Boolean {
        return intent != null && intent.action != null && intent.action == action
    }

    //invoked by user pressing the pause action button in notification
    private fun pauseWorkout(
        pendingStopIntent: PendingIntent,
        pendingResumeIntent: PendingIntent
    ):Int{
        //change action buttons, we now turn pause button in start button,
        //stop stays the same

        builder!!.clearActions()
            .addAction(R.drawable.ic_start, "Resume", pendingResumeIntent)
            .addAction(R.drawable.ic_stop, "Stop", pendingStopIntent)

        updateNotification()
        settings.paused = true

        return START_NOT_STICKY

    }

    private fun stopWorkout():Int{
        mainHandler.removeMessages(0)
        mGpsLocationClient?.removeUpdates(locationListener)
        mGpsLocationClient = null
        sensorManager?.unregisterListener(this)
        stopSelf()
        notificationManager!!.cancel(1)
        return START_NOT_STICKY
    }

    private fun resumeWorkout(
        pendingPauseIntent: PendingIntent,
        pendingStopIntent: PendingIntent
    ):Int{
        builder!!.clearActions()
            .addAction(R.drawable.ic_pause, "Pause", pendingPauseIntent)
            .addAction(R.drawable.ic_stop, "Stop", pendingStopIntent)
        settings.paused = false
        updateNotification()
        return START_NOT_STICKY
    }

    private fun setupTimer() {
        mainHandler.post(object : Runnable {
            override fun run() {
                updateNotification()
                settings.time++
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun setupSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null) {
            println("no step sensor for this device")
            return
        }
        sensorManager?.registerListener(
            this,
            stepSensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private fun formatDurationTime(durationSeconds: Long) =
        durationSeconds.seconds.toComponents { hours, minutes, seconds, _ ->
            String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds,
            )
        }



    private fun updateNotification() {
        if(builder == null || notificationManager == null || settings.paused) {
            return
        }
        val titleContent = "%.2f".format(settings.distance / 1000) + " km"
        builder!!.setContentTitle(HtmlCompat.fromHtml(
            "<font color=\"" +
                    ContextCompat.getColor(this, R.color.orange_400) +
                    "\">" + titleContent + "</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY))
        builder!!.setContentText(
            formatDurationTime(settings.time) + " Steps ${settings.workoutSteps - settings.totalSteps}"
        )
        notificationManager!!.notify(1, builder!!.build())
    }
    var mGpsLocationClient: LocationManager? = null
    val mainHandler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {

        mGpsLocationClient = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager

        mGpsLocationClient!!.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        3000,
        10.0f,
        locationListener
        )


    }

    private val locationListener = android.location.LocationListener { it -> //handle location change
        if(settings.initialPosition.provider!!.isEmpty()) {
            settings.initialPosition.latitude = it.latitude
            settings.initialPosition.longitude = it.longitude
            settings.initialPosition.speed = it.speed
            settings.initialPosition.altitude = it.altitude
            settings.initialPosition.provider = "updated"
        }
        else {
            if(it.accuracy <= 11) {
                if(!settings.paused) {
                    settings.distance += settings.initialPosition.distanceTo(it)
                }

                settings.initialPosition = it
            }
        }
        updateNotification()
    }



    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) {
            return
        }

        if(settings.totalSteps == -1) {
            println("total steps are -1, setting to ${event.values[0].toInt()}")
            settings.totalSteps = event.values[0].toInt()
        }
        settings.workoutSteps = event.values[0].toInt()
        updateNotification()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}