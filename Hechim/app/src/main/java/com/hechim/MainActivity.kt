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
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.hechim.databinding.ActivityMainBinding
import com.hechim.di.SecureSharedPref
import com.hechim.models.local.AppLocale
import com.hechim.models.repo.NavigationRepository
import com.hechim.services.TestForeground
import com.hechim.utils.Extensions.animatedNavigate
import com.hechim.view.OnBoardingFragmentDirections
import com.hechim.view.RegisterFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var injectedSharedPref: SecureSharedPref

    @Inject
    lateinit var navigationRepository: NavigationRepository

    override fun attachBaseContext(newBase: Context?) {
        val secureSharedPref = SecureSharedPref(newBase!!)
        super.attachBaseContext(updateBaseContextLocale(context = newBase, secureSharedPref = secureSharedPref))
    }

    private fun updateBaseContextLocale(context: Context, secureSharedPref: SecureSharedPref): Context? {
        val lang = secureSharedPref.getStringValue(SecureSharedPref.locale)
        val language: String = lang ?: AppLocale.English.locale
        val locale = Locale(language)
        Locale.setDefault(locale)
        return updateResourcesLocale(context, locale)
    }

    private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        supportActionBar?.hide();

        createNotification()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navigationRepository.setController(navController)





        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener {
            if(it != null) {
                val uri = it.link
                if(uri != null) {

                    val s = uri.getQueryParameter("curPage")
                    val code = uri.getQueryParameter("code")!!.toInt()
                    val email = uri.getQueryParameter("email")!!.toString()
                    navHostFragment.navController.animatedNavigate(
                        OnBoardingFragmentDirections.actionOnBoardingFragmentToCodeFragment(
                            code = code,
                            email = email
                        )
                    )
                }
            }
        }


    }


//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        if(intent != null) {
//            println("on new intent is not null")
//            val uri = intent.data
//            if(uri != null) {
//                println("uri is not null")
//                val code = uri.getQueryParameter("code")!!.toInt()
//                val email = uri.getQueryParameter("email")!!.toString()
//                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//                navHostFragment.navController.animatedNavigate(
//                    OnBoardingFragmentDirections.actionOnBoardingFragmentToCodeFragment(
//                        code = code,
//                        email = email
//                    )
//                )
//
//
//            }
//            else {
//                println("uri is null")
//            }
//        }
//        else {
//            println("on new intent is null")
//        }
//    }

    private fun stopServiceMy(intent: Intent) {
        stopService(intent)
    }

    private fun createNotification() {
        val name = "RideArrival"
        val descriptionText = "Channel for Ride Arriving"
        val importance = NotificationManager.IMPORTANCE_MIN
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