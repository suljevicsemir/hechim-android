package com.hechim.models.data.auth
import com.google.gson.annotations.SerializedName

data class TokenPair(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
