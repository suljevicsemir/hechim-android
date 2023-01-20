package com.hechim.view_models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.hechim.models.local.AppPermission
import com.hechim.models.local.HechimAppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
):ViewModel() {

    private val settings: HechimAppSettings = HechimAppSettings

    var appPermissions = listOf<AppPermission>()

    init {
        appPermissions = settings.buildPermissions(context).filter {
            !it.granted
        }
    }

}