package com.hechim

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import com.hechim.databinding.ActivityMainBinding
import com.hechim.di.SecureSharedPref
import com.hechim.models.local.AppLocale
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var injectedSharedPref: SecureSharedPref

    override fun attachBaseContext(newBase: Context?) {
        val secureSharedPref = SecureSharedPref(newBase!!)
        super.attachBaseContext(updateBaseContextLocale(context = newBase, secureSharedPref = secureSharedPref))
    }

    private fun updateBaseContextLocale(context: Context, secureSharedPref: SecureSharedPref): Context? {
        val lang = secureSharedPref.getStringValue(SecureSharedPref.locale)
        val language: String = lang ?: AppLocale.English.locale
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)
    }

    private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide();

    }
}