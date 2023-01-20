package com.hechim.models.local




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
        "bs" -> AppLocale.Bosnian
        "it" -> AppLocale.Italian
        "de" -> AppLocale.German
        else -> AppLocale.English
    }
}
