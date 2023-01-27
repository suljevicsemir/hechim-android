package com.hechim.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.auth0.android.jwt.Claim
import com.auth0.android.jwt.JWT
import com.hechim.models.data.auth.TokenPair

class SecureSharedPref(context: Context) {


    private var sharedPref: SharedPreferences

    init {
        println("debug: Init block of Secure shared pref")
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        sharedPref = EncryptedSharedPreferences.create(
            context,
            "name",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        const val refreshTokenKey = "refreshToken"
        const val accessTokenKey = "accessToken"
        const val isLoggedInKey = "isLoggedIn"
        const val userId = "userId"
        const val biometricsSet = "biometricsSet"
        const val locale = "locale"
    }

    fun storeLoginInfo(tokenPair: TokenPair) {
        val jwt = JWT(tokenPair.accessToken)
        val id: Claim = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier")
        storeIntValue(userId, id.asInt()!!)
        storeStringValue(accessTokenKey, tokenPair.accessToken)
        storeStringValue(refreshTokenKey, tokenPair.refreshToken)
        setBooleanValue(isLoggedInKey, true)
    }

    fun storeStringValue(key: String, value: String) {
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(key: String, defaultValue: String? = null ) : String? {
        return sharedPref.getString(key, defaultValue)
    }

    private fun setBooleanValue(key: String, value: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun getIntValue(key: String, defaultValue: Int = 0): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    private fun storeIntValue(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun deleteAll() {
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }



}