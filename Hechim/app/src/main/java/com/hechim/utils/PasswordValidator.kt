package com.hechim.utils


import android.util.Patterns
import dagger.hilt.android.scopes.ActivityScoped
import java.util.regex.Pattern


@ActivityScoped
class PasswordValidator {
    private val passwordPattern : Pattern = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[@#$%^&+=!*(){}])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$")

    fun isValidEmail(email: String?): Boolean {
        return if(email == null || email.isEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    fun isPasswordValid(password: String): Boolean {
        return passwordPattern.matcher(password).matches()
    }

    fun isNewPasswordValid(oldPassword: String, newPassword: String): Boolean {
        return isPasswordValid(newPassword) && oldPassword.compareTo(newPassword) != 0
    }

    fun isConfirmedPasswordValid(newPassword: String, confirmedPassword: String): Boolean {
        return newPassword.compareTo(confirmedPassword) == 0
    }

    fun containsLength(text: String) = text.trim().length in 8..15
    fun containsSpecialCharacters(text: String) = text.any { it in "@#\\$%^&+=!.*(){}[]" }
    fun containsLowercase(text: String) = text.any(Char::isLowerCase)
    fun containsUpperCase(text: String) = text.any(Char::isUpperCase)
    fun containsNumber(text: String) = text.any(Char::isDigit)
}