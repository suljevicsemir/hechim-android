package com.hechim.models.repo

import android.util.Log
import androidx.datastore.core.DataStore
import com.hechim.dev.AppSettings
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class AppSettingsRepository @Inject constructor(
    private val dataStore: DataStore<AppSettings>
){

    val readAppSettings: Flow<AppSettings> = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                Log.d("Error reading proto ", exception.message.toString())
                emit(AppSettings.getDefaultInstance())
            }
            else {
                throw exception
            }
        }

    suspend fun updateOnBoarding(value: Boolean) {
        dataStore.updateData {
            it.toBuilder().setFinishedOnBoarding(value).build()
        }
    }

    suspend fun updateDarkTheme(value: Boolean) {
        dataStore.updateData {
            it.toBuilder().setDarkTheme(value).build()
        }
    }
}