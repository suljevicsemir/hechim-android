package com.hechim.models.data.auth
import com.google.gson.annotations.SerializedName

data class TokenPair(
    @SerializedName("access")
    val accessToken: String,
    @SerializedName("refresh")
    val refreshToken: String
)
