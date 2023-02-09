package com.hechim.utils.errors

class NoInternetException(
    title: Int? = null,
    description: Int? = null,
    button: Int? = null,
    message: String? = null,

    cause: Throwable? = null) : Exception(message, cause)