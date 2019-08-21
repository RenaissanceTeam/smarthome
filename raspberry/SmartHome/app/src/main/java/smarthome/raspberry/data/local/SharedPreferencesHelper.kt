package smarthome.raspberry.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val HOME_ID_KEY: String = "home_id_key"
const val DEFAULT_HOME_ID: String = "default_home_id"

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("smarthome", MODE_PRIVATE)

    fun hasSavedHomeId(): Boolean {
         return sharedPreferences.getString(HOME_ID_KEY, DEFAULT_HOME_ID) != DEFAULT_HOME_ID
    }

    fun getHomeId(): String {
        return sharedPreferences.getString(HOME_ID_KEY, null) ?: DEFAULT_HOME_ID
    }

    @SuppressLint("ApplySharedPref")
    suspend fun setHomeId(homeId: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(HOME_ID_KEY, homeId).commit()
        }
    }
}