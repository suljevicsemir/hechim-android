package com.hechim.di

import android.content.Context
import com.hechim.models.NavigationRepository
import com.hechim.utils.PasswordValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    fun providePasswordValidator() = PasswordValidator()

    @Singleton
    @Provides
    fun providerNavigationRepository(@ApplicationContext context: Context) = NavigationRepository(context)
}