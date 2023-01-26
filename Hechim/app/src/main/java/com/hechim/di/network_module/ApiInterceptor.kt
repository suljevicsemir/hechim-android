package com.hechim.di.network_module

import com.hechim.R
import com.hechim.di.SecureSharedPref
import com.hechim.models.local.AppLocale
import com.hechim.models.repo.NavigationRepository
import com.hechim.utils.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiInterceptor @Inject constructor(
    private val navigationRepository: NavigationRepository,
    private val secureSharedPref: SecureSharedPref
): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder().addHeader(HttpHeaders.acceptLanguage, secureSharedPref.getStringValue(SecureSharedPref.locale) ?: AppLocale.English.locale).build()

        println(request.url)
        val response = chain.proceed(request = request)

        if(response.code == 401) {
            if(secureSharedPref.getBooleanValue(SecureSharedPref.isLoggedInKey)) {
                secureSharedPref.deleteAll()
                runBlocking {
                    withContext(Dispatchers.Main) {
                        navigationRepository.navigateAndRemove(R.id.loginFragment)
                    }
                }
            }
        }
        if(response.code == 401) {
            println("ended with 401 from interceptor")
        }
        if(response.code == 404) {
            println("enede dwith 404 from interceptor")
            println(response)
        }
        return  response
    }
}