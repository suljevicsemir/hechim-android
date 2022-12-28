package com.hechim.models.local

data class AppLanguageItem(
    val image: Int,
    val text: Int,
    val locale: AppLocale,
    var selected: Boolean = false,
)
