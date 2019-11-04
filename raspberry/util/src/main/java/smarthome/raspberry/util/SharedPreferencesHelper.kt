package smarthome.raspberry.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("smarthome", MODE_PRIVATE)

    @SuppressLint("ApplySharedPref")
    suspend fun setString(key: String, value: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(key, value).commit()
        }
    }

    fun getString(key: String, default: String = ""): String {
        return sharedPreferences.getString(key, default) ?: default
    }
}