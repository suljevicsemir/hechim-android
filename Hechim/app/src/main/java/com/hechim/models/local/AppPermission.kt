package com.hechim.models.local

data class AppPermission(
    val string: String,
    var granted: Boolean,
    val title: Int,
    val description: Int,
    val image: Int = 0
)
