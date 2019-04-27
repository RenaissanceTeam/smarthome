package smarthome.raspberry.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("", MODE_PRIVATE)

    fun isHomeIdExists(): Boolean {
         return sharedPreferences.getString(HOME_ID_KEY, DEFAULT_HOME_ID) != DEFAULT_HOME_ID
    }

    fun getHomeId(): String {
        return sharedPreferences.getString(HOME_ID_KEY, DEFAULT_HOME_ID)!!
    }

    fun setHomeId(homeId: String) {
        sharedPreferences.edit().putString(HOME_ID_KEY, homeId).apply()
    }

    fun getDoNotDisturb(): Boolean {
        return sharedPreferences.getBoolean(DO_NOT_DISTURB_KEY, false)
    }

    fun setDoNotDisturb(mode: Boolean) {
        sharedPreferences.edit().putBoolean(DO_NOT_DISTURB_KEY, mode).apply()
    }

    companion object {
        const val HOME_ID_KEY: String = "home_id_key"
        const val DEFAULT_HOME_ID: String = "default_hom_id"

        const val DO_NOT_DISTURB_KEY: String = "do_not_disturb_key"

        private var instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            if (instance == null)
                instance = SharedPreferencesHelper(context)

            return instance as SharedPreferencesHelper
        }
    }
}