package com.hechim.models.repo

import com.hechim.di.network_module.BaseRepository
import com.hechim.models.data.Resource
import com.hechim.models.data.auth.*
import com.hechim.models.interfaces.api.AuthenticationAPI
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AuthenticationRepository @Inject constructor(
    private val authenticationAPI: AuthenticationAPI,
): BaseRepository(){
    suspend fun register(userRegister: UserRegister): Resource<UserConfirmedRegister> {
        return backendMiddleware { authenticationAPI.register(userRegister) }
    }

    suspend fun login(userLogin: UserLogin): Resource<TokenPair>{
        return backendMiddleware { authenticationAPI.login(userLogin) }
    }

    suspend fun confirmEmail(confirmEmail: ConfirmEmail): Resource<TokenPair>{
        return backendMiddleware { authenticationAPI.confirmEmail(confirmEmail) }
    }

    suspend fun refreshToken(tokenPair: TokenPair): Resource<TokenPair>{
        return backendMiddleware { authenticationAPI.refreshToken(tokenPair) }
    }
}
