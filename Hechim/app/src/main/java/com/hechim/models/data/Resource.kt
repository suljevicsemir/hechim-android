package com.hechim.models.data

sealed class Resource<T>(
    val data: T? = null,
    val message: String = "",
) {
    class Success<T>(data: T?, message: String = "") : Resource<T>(data = data, message = message)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T>(message: String = "",) : Resource<T>(message = message,)
    class Nothing<T>(): Resource<T>()
}

data class APIResponse<T>(
    val data: T?,
    val error: APIError,
    val success: Boolean = false
)

data class APIError(
    val message: String?,
    val errors: List<String>?
)