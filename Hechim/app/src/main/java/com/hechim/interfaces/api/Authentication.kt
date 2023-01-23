package com.hechim.interfaces.api

import com.hechim.models.data.auth.UserRegister
import com.hechim.utils.HttpHeaders
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Authentication {
    @POST(value = "Authentication/register")
    suspend fun register(
        @Body userRegister: UserRegister,
        @Header(HttpHeaders.authorizationHeader) token: String?
    )

    @POST(value = "Authentication/login")
    suspend fun login()

    @POST(value = "Authentication/refresh-token")
    suspend fun refreshToken()
}