package com.hechim.models.local

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val locale: AppLocale? = null,
    val isFirstRun: Boolean = true
)

data class AppLanguageModel(
    val image: Int,
    val text: String,
    val locale: AppLocale,
    val selected: Boolean,
)


enum class AppLocale(val locale: String) {
    English("en"),
    French("fr"),
    Italian("it"),
    German("de"),
    Bosnian("bs"),
}

fun String?.toAppLocale() : AppLocale{

    return when(this) {
        "fr" -> AppLocale.French
        "en" -> AppLocale.English
        "it" -> AppLocale.Italian
        "de" -> AppLocale.German
        else -> AppLocale.Bosnian
    }
}
