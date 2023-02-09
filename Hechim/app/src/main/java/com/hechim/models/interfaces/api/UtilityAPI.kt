package com.hechim.models.interfaces.api

import retrofit2.http.GET

interface UtilityAPI {

    @GET("Utility/10sec-timeout")
    suspend fun produceTimeout()
    @GET("Utility/500error")
    suspend fun produceInternalServerError()
}