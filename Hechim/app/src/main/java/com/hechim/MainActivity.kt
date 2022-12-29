package com.hechim

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.media.session.MediaSession
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import com.hechim.databinding.ActivityMainBinding
import com.hechim.di.SecureSharedPref
import com.hechim.models.local.AppLocale
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var injectedSharedPref: SecureSharedPref

    override fun attachBaseContext(newBase: Context?) {
        val secureSharedPref = SecureSharedPref(newBase!!)
        super.attachBaseContext(updateBaseContextLocale(context = newBase, secureSharedPref = secureSharedPref))
    }

    private fun updateBaseContextLocale(context: Context, secureSharedPref: SecureSharedPref): Context? {
        val lang = secureSharedPref.getStringValue(SecureSharedPref.locale)
        val language: String = lang ?: AppLocale.English.locale
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)
    }

    private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();

        createNotification()
        showNotification()

    }

    private fun createNotification() {
        val name = "RideArrival"
        val descriptionText = "Channel for Ride Arriving"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("RideArrival", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    private fun showNotification() {
        val contentView = RemoteViews(packageName, R.layout.workout_notification_layout)



        val builder = NotificationCompat.Builder(this, "RideArrival")
            .setSmallIcon(R.drawable.ic_launcher_foreground)

            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1))
            .addAction(R.drawable.ic_not_started, "s", null)
            .addAction(R.drawable.ic_pause, "s", null)

            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(), PendingIntent.FLAG_IMMUTABLE))

        builder.build().getLargeIcon().apply {

        }

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}