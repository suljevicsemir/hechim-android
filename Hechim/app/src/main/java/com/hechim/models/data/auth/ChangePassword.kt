package com.hechim.models.data.auth

data class ChangePassword(
    val oldPassword: String,
    val newPassword: String
)
