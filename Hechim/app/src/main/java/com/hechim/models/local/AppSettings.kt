package com.hechim.models.local

import android.Manifest
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.hechim.R
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
object HechimAppSettings {
    //current device Android API level
    private val sdkVersion = Build.VERSION.SDK_INT

    //check for activity recognition permission, used for step counting
    //only available on Android 10 and above (API 29)
    //version below 10 don't have this hardware, functionality disabled
    val shouldCheckActivity: Boolean = sdkVersion >= 29

    //check for notifications permissions
    //check only for Android 13 and above (API LEVEL 33)
    //versions below already have that permission
    val shouldCheckNotifications: Boolean = sdkVersion >= 33

    //check for background location permission
    //check only for Android 10 and above (API LEVEL 29)
    //versions below already have that permission
    val shouldCheckBackgroundLocation: Boolean = sdkVersion >= 29



    fun buildPermissions(context: Context) : List<AppPermission>{
        val list = mutableListOf<AppPermission>()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            list.add(AppPermission(
                string = Manifest.permission.POST_NOTIFICATIONS,
                granted = permissionGranted(context, Manifest.permission.POST_NOTIFICATIONS),
                title = R.string.permission_notification_title,
                description = R.string.permission_notification_description,
                image = R.drawable.notification_permission
                )
            )
        }
        list.add(AppPermission(
            string = Manifest.permission.ACCESS_FINE_LOCATION,
            granted = permissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION),
            title = R.string.permission_location_title,
            description = R.string.permission_location_description,
            image = R.drawable.location_permission
        ))
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            list.add(AppPermission(
                string = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                granted = permissionGranted(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                title = R.string.permission_background_location_title,
                description = R.string.permission_background_location_desc,
                image = R.drawable.location_permission
            ))
            list.add(AppPermission(
                string = Manifest.permission.ACTIVITY_RECOGNITION,
                granted = permissionGranted(context, Manifest.permission.ACTIVITY_RECOGNITION),
                title = R.string.permission_activity_title,
                description = R.string.permission_activity_description,
                image = R.drawable.activity_permission
                )
            )
        }
        return list
    }

    private fun permissionGranted(context: Context, string: String):Boolean{
        return ContextCompat.checkSelfPermission(context, string) == PackageManager.PERMISSION_GRANTED
    }

}