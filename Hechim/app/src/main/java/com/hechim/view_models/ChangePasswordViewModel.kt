package com.hechim.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.hechim.di.SecureSharedPref
import com.hechim.models.repo.AuthenticationRepository
import com.hechim.models.repo.NavigationRepository
import com.hechim.utils.PasswordValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val passwordValidator: PasswordValidator
): ViewModel(){

    private val _passwordValidation: MutableStateFlow<PasswordValidation> = MutableStateFlow(PasswordValidation())
    val passwordValidation: StateFlow<PasswordValidation> = _passwordValidation.asStateFlow()


    fun onPasswordChanged(text: String) {
        println("Password changed")
        _passwordValidation.value = PasswordValidation(
            length = passwordValidator.containsLength(text),
            containsSpecialCharacter = passwordValidator.containsSpecialCharacters(text),
            containsNumber = passwordValidator.containsNumber(text),
            containsUppercaseLetter = passwordValidator.containsUpperCase(text),
            containsLowercaseLetter = passwordValidator.containsLowercase(text)
        )
    }

}

data class PasswordValidation(
    val length: Boolean = false,
    val containsSpecialCharacter: Boolean = false,
    val containsNumber: Boolean = false,
    val containsUppercaseLetter: Boolean = false,
    val containsLowercaseLetter: Boolean = false,
)