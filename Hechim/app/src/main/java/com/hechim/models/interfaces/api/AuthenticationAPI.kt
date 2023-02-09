package com.hechim.models.interfaces.api


import com.hechim.models.data.APIResponse
import com.hechim.models.data.auth.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationAPI {
    @POST(value = "Authentication/register")
    suspend fun register(@Body userRegister: UserRegister): Response<APIResponse<UserConfirmedRegister>>

    @POST(value = "Authentication/login")
    suspend fun login(@Body userLogin: UserLogin): Response<APIResponse<TokenPair>>

    @POST(value = "Authentication/confirm-email")
    suspend fun confirmEmail(@Body confirmEmail: ConfirmEmail): Response<APIResponse<TokenPair>>

    @POST(value = "Authentication/refresh-token")
    suspend fun refreshToken(@Body tokenPair: TokenPair): Response<APIResponse<TokenPair>>

    @GET("Utility/10sec-timeout")
    suspend fun produceTimeout()
    @GET("Utility/500error")
    suspend fun produceInternalServerError()




}