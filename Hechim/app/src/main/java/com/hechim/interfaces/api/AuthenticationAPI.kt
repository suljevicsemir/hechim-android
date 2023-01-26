package com.hechim.interfaces.api

import com.google.gson.JsonObject
import com.hechim.models.data.APIResponse
import com.hechim.models.data.Resource
import com.hechim.models.data.auth.*
import okhttp3.Call
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface AuthenticationAPI {
    @POST(value = "Authentication/register")
    suspend fun register(@Body userRegister: UserRegister): APIResponse<UserConfirmedRegister>

    @POST(value = "Authentication/login")
    suspend fun login(@Body userLogin: UserLogin): APIResponse<TokenPair>

    @POST(value = "Authentication/confirm-email")
    suspend fun confirmEmail(@Body confirmEmail: ConfirmEmail): Resource<ConfirmEmail>

    @POST(value = "Authentication/refresh-token")
    suspend fun refreshToken(@Body tokenPair: TokenPair): Resource<TokenPair>




}