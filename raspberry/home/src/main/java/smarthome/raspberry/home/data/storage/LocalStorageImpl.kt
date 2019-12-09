package smarthome.raspberry.home.data.storage

import smarthome.raspberry.util.SharedPreferencesHelper

class LocalStorageImpl(private val preferences: SharedPreferencesHelper):
    LocalStorage {
    
    override suspend fun saveHome(homeId: String) {
        preferences.setString(HOME_ID, homeId)
    }
    
    companion object {
        private const val HOME_ID = "HOME_ID"
    }
}