package com.hechim.di.network_module

import android.content.Context
import com.hechim.BuildConfig
import com.hechim.di.SecureSharedPref
import com.hechim.interfaces.api.AuthenticationAPI
import com.hechim.models.repo.AuthenticationRepository
//import com.hechim.models.repo.AuthenticationRepository
import com.hechim.models.repo.NavigationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthorizationModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: ApiInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthenticationAPI(
        okHttpClient: OkHttpClient
    ) : AuthenticationAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(AuthenticationAPI::class.java)
    }


    @Singleton
    @Provides
    fun provideAuthenticationRepository(
        authenticationAPI: AuthenticationAPI,
        secureSharedPref: SecureSharedPref)
    = AuthenticationRepository(authenticationAPI, secureSharedPref)


}