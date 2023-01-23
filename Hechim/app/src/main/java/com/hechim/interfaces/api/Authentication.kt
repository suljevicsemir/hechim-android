package com.hechim.interfaces.api

import com.hechim.models.data.auth.ConfirmEmail
import com.hechim.models.data.auth.TokenPair
import com.hechim.models.data.auth.UserLogin
import com.hechim.models.data.auth.UserRegister
import retrofit2.http.Body
import retrofit2.http.POST

interface Authentication {
    @POST(value = "Authentication/register")
    suspend fun register(@Body userRegister: UserRegister)

    @POST(value = "Authentication/login")
    suspend fun login(@Body userLogin: UserLogin)

    @POST(value = "Authentication/confirm-email")
    suspend fun confirmEmail(@Body confirmEmail: ConfirmEmail)

    @POST(value = "Authentication/refresh-token")
    suspend fun refreshToken(@Body tokenPair: TokenPair)
}