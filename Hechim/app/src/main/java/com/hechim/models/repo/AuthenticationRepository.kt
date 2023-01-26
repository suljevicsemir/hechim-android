package com.hechim.models.repo

import com.hechim.di.SecureSharedPref
import com.hechim.interfaces.api.AuthenticationAPI
import com.hechim.models.data.APIResponse
import com.hechim.models.data.Resource
import com.hechim.models.data.auth.*
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AuthenticationRepository @Inject constructor(
    private val authenticationAPI: AuthenticationAPI,
    private val secureSharedPref: SecureSharedPref
){
    suspend fun register(userRegister: UserRegister): Resource<UserConfirmedRegister> {
        val response = try {
            authenticationAPI.register(userRegister)
        }catch (e: Exception) {
            return Resource.Error(e.message!!)
        }
        return Resource.Success(
            data = response.data
        )
    }

    suspend fun login(userLogin: UserLogin): Resource<TokenPair>{
        val response: APIResponse<TokenPair> = try {
            authenticationAPI.login(userLogin)
        }catch (e: Exception){
            return Resource.Error("s")
        }
        return Resource.Success(
            data = response.data,
        )
    }

    suspend fun confirmEmail(confirmEmail: ConfirmEmail): Resource<ConfirmEmail>{
        return try {
            authenticationAPI.confirmEmail(confirmEmail)
        }catch (e:Exception) {
            return Resource.Error(e.message!!)
        }
    }

    suspend fun refreshToken(tokenPair: TokenPair): Resource<TokenPair>{
        return try {
            authenticationAPI.refreshToken(tokenPair)
        }catch (e: Exception) {
            return Resource.Error(e.message!!)
        }
    }


}