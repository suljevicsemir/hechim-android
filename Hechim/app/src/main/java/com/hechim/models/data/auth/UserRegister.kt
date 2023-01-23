package com.hechim.models.data.auth


data class UserRegister(
    val email: String,
    val password: String,
    val confirmPassword: String,
    val username: String
)
