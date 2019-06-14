package smarthome.client.util

import android.content.Context
import android.util.Log
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.model.Model

class FcmTokenStorage(context: Context) {
    val INVALID_TOKEN = ""
    private val TAG = "FcmTokenStorage"
    private val PREFS_NAME = "FcmTokenStorage"
    private val STORAGE_KEY = "token"
    private var prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var savedToken: String
        get() = prefs.getString(STORAGE_KEY, INVALID_TOKEN)!!
        set(token) {
            if (DEBUG) Log.d(TAG, "saving new token=$token")

            prefs.edit().putString(STORAGE_KEY, token).apply()
            Model.saveInstanceToken(token, this)
        }

    val isSaved = prefs.contains(STORAGE_KEY)

    fun removeToken() = prefs.edit().remove(STORAGE_KEY).apply()
}