package com.hechim.di.network_module

//import com.hechim.models.repo.AuthenticationRepository
import android.content.Context
import com.hechim.BuildConfig
import com.hechim.models.interfaces.api.AuthenticationAPI
import com.hechim.models.repo.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
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
            .callTimeout(Duration.ofMillis(5000))
            //.readTimeout(Duration.ofSeconds(5))
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
    fun provideAuthenticationRepository(authenticationAPI: AuthenticationAPI) = AuthenticationRepository(authenticationAPI)

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ) = AppConnectivity(context)


}