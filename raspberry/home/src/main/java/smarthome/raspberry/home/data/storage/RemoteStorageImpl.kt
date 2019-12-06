package smarthome.raspberry.home.data.storage

import smarthome.library.datalibrary.api.HomesReferencesStorage
import smarthome.library.datalibrary.api.SmartHomeStorage

class RemoteStorageImpl(
        private val homesReferencesStorage: smarthome.library.datalibrary.api.HomesReferencesStorage,
        private val homeStorage: smarthome.library.datalibrary.api.SmartHomeStorage
) : RemoteStorage {
    
    override suspend fun isHomeIdUnique(homeId: String) =
        homesReferencesStorage.checkIfHomeExists(homeId).not()
    
    override suspend fun saveHome(homeId: String) {
        homesReferencesStorage.addHomeReference(homeId)
        homeStorage.createSmartHome()
    }
}