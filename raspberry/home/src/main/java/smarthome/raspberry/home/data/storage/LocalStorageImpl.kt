package smarthome.raspberry.home.data.storage

import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.set

class LocalStorageImpl(private val preferences: StorageHelper):
    LocalStorage {
    
    override suspend fun saveHome(homeId: String) {
        preferences.set(HOME_ID, homeId)
    }
    
    companion object {
        private const val HOME_ID = "HOME_ID"
    }
}