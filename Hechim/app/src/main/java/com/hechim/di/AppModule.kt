package com.hechim.di

//import com.hechim.models.repo.ProtoRepository

import android.content.Context
import androidx.datastore.core.DataStore
import com.hechim.dev.AppSettings
import com.hechim.di.network_module.AppConnectivity
import com.hechim.models.repo.AppSettingsRepository
import com.hechim.models.repo.NavigationRepository
import com.hechim.utils.PasswordValidator
import com.hechim.view_models.PermissionsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "user_prefs.pb"
private const val USER_PREFERENCES_NAME = "user_preferences"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSecureSharedPref(
        @ApplicationContext context: Context
    ) = SecureSharedPref(context = context)

    @Singleton
    @Provides
    fun providePermissionsViewModel(
        @ApplicationContext context: Context
    ) = PermissionsViewModel(context)

    @Singleton
    @Provides
    fun providePasswordValidator() = PasswordValidator()

    @Singleton
    @Provides
    fun providerNavigationRepository(@ApplicationContext context: Context) = NavigationRepository(context)

    @Singleton
    @Provides
    fun provideAppSettingsRepository(dataStore: DataStore<AppSettings>) = AppSettingsRepository(dataStore = dataStore)

    @Singleton
    @Provides
    fun provideAppConnectivity(@ApplicationContext context: Context) = AppConnectivity(context)




}
