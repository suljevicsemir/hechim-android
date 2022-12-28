package com.hechim.view_models

import androidx.lifecycle.ViewModel
import com.hechim.R
import com.hechim.di.SecureSharedPref
import com.hechim.models.data.Resource
import com.hechim.models.local.AppLanguageItem
import com.hechim.models.local.AppLocale
import com.hechim.models.local.toAppLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//singleton object that holds all the languages
//the app supports
object AppLanguages {
    val languages = listOf(
        AppLanguageItem(
            image = R.drawable.ic_language_fr,
            text = R.string.app_language_fr,
            locale = AppLocale.French,
        ),
        AppLanguageItem(
            image = R.drawable.ic_language_en,
            text = R.string.app_language_en,
            locale = AppLocale.English,
        ),
        //german and italian languages aren't supported
        //this is only to practice displaying of the app bar
        AppLanguageItem(
            image = R.drawable.ic_language_de,
            text = R.string.app_language_de,
            locale = AppLocale.German,
        ),
        AppLanguageItem(
            image = R.drawable.ic_language_it,
            text = R.string.app_language_it,
            locale = AppLocale.Italian,
        ),
        AppLanguageItem(
            image = R.drawable.ic_language_it,
            text = R.string.app_language_bs,
            locale = AppLocale.Bosnian,
        )
    )
    //updates the selected flag for every the list
    //after it was loaded / set from the view model
    fun updateLanguage(locale: AppLocale) {
        languages.forEach {
            it.selected = it.locale == locale
        }
    }
}

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
    private val secureSharedPref: SecureSharedPref
) : ViewModel() {

    private val _selectedLocale = MutableStateFlow(AppLocale.English)
    val selectedLocale: StateFlow<AppLocale> = _selectedLocale.asStateFlow()

    //list of languages and their selected value that is used to display
    //in the fragment list
    private var _languages: MutableStateFlow<Resource<List<AppLanguageItem>>> = MutableStateFlow(Resource.Success(AppLanguages.languages))
    val languages: StateFlow<Resource<List<AppLanguageItem>>> = _languages.asStateFlow()


    init {
        //initialize selected locale from secure shared pref
        //default value is english
        _selectedLocale.value = secureSharedPref.getStringValue(SecureSharedPref.locale).toAppLocale()
        //update languages flags
        AppLanguages.updateLanguage(_selectedLocale.value)
        _languages.value = Resource.Success(AppLanguages.languages)
    }

    //method called from the fragment's dialog to confirm
    //the selected language
    fun selectAndConfirm(appLocale: AppLocale) {
        //update local value, trigger listener
        _selectedLocale.value = appLocale
        //store in shared prefs
        secureSharedPref.storeStringValue(SecureSharedPref.locale, _selectedLocale.value.locale)
        //update languages flags
        AppLanguages.updateLanguage(_selectedLocale.value)
        _languages.value = Resource.Success(AppLanguages.languages)
    }
}
