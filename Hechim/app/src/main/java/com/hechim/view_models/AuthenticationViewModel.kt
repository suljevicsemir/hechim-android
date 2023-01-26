package com.hechim.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hechim.di.SecureSharedPref
import com.hechim.models.data.Resource
import com.hechim.models.data.auth.*
import com.hechim.models.repo.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val secureSharedPref: SecureSharedPref
): ViewModel(){

    private val _registerResource: MutableStateFlow<Resource<UserConfirmedRegister>> = MutableStateFlow(Resource.Nothing())
    val registerResource: StateFlow<Resource<UserConfirmedRegister>> = _registerResource.asStateFlow()

    private val _loginResource: MutableStateFlow<Resource<TokenPair>> = MutableStateFlow(Resource.Nothing())
    val loginResource: StateFlow<Resource<TokenPair>> = _loginResource.asStateFlow()

    private val _confirmEmailResource: MutableStateFlow<Resource<ConfirmEmail>> = MutableStateFlow(Resource.Nothing())
    val confirmEmailResource: StateFlow<Resource<ConfirmEmail>> = _confirmEmailResource.asStateFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        _registerResource.value = Resource.Loading()
        println("registering with $email $password $confirmPassword")
        viewModelScope.launch {
            _registerResource.value = authenticationRepository.register(
                UserRegister(email.trim(), password.trim(), confirmPassword.trim(), email.trim()))
            if(_registerResource.value.data != null) {
                println(_registerResource.value.data.toString())
            }
            else {
                println(_registerResource.value.message)
            }
        }
    }

    fun login(email: String, password: String) {
        _loginResource.value = Resource.Loading("")
        viewModelScope.launch {
            val result = authenticationRepository.login(UserLogin(email.trim(), password.trim()))
            if(result is Resource.Success) {
                secureSharedPref.storeLoginInfo(result.data!!)
            }
            _loginResource.value = result
        }
    }

    fun confirmEmail(code: Int, email: String) {
        _confirmEmailResource.value = Resource.Loading("null")
        viewModelScope.launch {
            _confirmEmailResource.value = authenticationRepository.confirmEmail(ConfirmEmail(code,email))
        }
    }

}