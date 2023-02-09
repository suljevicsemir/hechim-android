package com.hechim.di.network_module

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hechim.R
import com.hechim.models.data.APIResponse
import com.hechim.models.data.ErrorSpecification
import com.hechim.models.data.Resource
import com.hechim.utils.errors.NoInternetException
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ActivityRetainedScoped
open class BaseRepository {

    suspend fun <T: Any> backendMiddleware(callback: suspend () -> Response<APIResponse<T>>): Resource<T> {
        lateinit var res: Response<APIResponse<T>>
        try {
            res = callback.invoke()
        }
        //custom exception when internet connectivity isn't available
        //this catch block will be triggered when exception is thrown from the ApiInterceptor
        //the error is recognized by the Fragment's callback and error content
        //is displayed
        catch (e: NoInternetException) {
            return Resource.Error(
                errorSpecification = ErrorSpecification(
                    useStringResource = true,
                    titleResource = R.string.no_internet_title,
                    descriptionResource = R.string.no_internet_title,
                    retryText = R.string.no_internet_button
                )
            )
        }
        catch (e: HttpException) {
            if(e.message() == "timeout") {
                return Resource.Error(
                    errorSpecification = ErrorSpecification(
                        useStringResource = true,
                        titleResource = R.string.timeout_error_title,
                        descriptionResource = R.string.timeout_error_description,
                        retryText = R.string.timeout_error_retry
                    )
                )
            }
            return Resource.Error(
                errorSpecification = ErrorSpecification(
                    useStringResource = true,
                    titleResource = R.string.http_error_title,
                    descriptionResource = R.string.http_error_description,
                    retryText = R.string.http_error_retry
                )
            )
        }
        catch (e: IOException) {
            return Resource.Error(
                errorSpecification = ErrorSpecification(
                    useStringResource = true,
                    titleResource = R.string.io_error_title,
                    descriptionResource = R.string.io_error_description,
                    retryText = R.string.io_error_retry
                ),
            )
        }

        catch (e: Exception) {
            val code = res.code()
            return Resource.Error(
                errorSpecification = ErrorSpecification(
                    useStringResource = true,
                    titleResource = if(code >= 500) R.string.internal_server_error_title else R.string.unexpected_error_title,
                    descriptionResource = if(code >= 500)  R.string.internal_server_error_description else R.string.unexpected_error_description,
                    retryText = if(code >= 500) R.string.internal_server_error_retry else R.string.unexpected_error_retry
                ),
            )
        }

        if(res.isSuccessful) {
            val t: APIResponse<T>? = res.body()
            if(t!!.success && t.data != null) {
                return Resource.Success(
                    data = t.data
                )
            }
        }

        val responseBody = res.errorBody()!!.string()
        val listType = object : TypeToken<APIResponse<T>>() {}.type

        val apiResponse: APIResponse<T> = Gson().fromJson(responseBody, listType)
        val apiError = apiResponse.error




        return Resource.Error(
            errorSpecification = ErrorSpecification(
                useStringResource = false,
                title = "Validation error",
                description = if(apiError == null) "" else apiError.message,
            )
        )


    }
}