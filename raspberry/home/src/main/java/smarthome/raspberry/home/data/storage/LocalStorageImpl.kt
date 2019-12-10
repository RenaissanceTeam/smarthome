package smarthome.raspberry.home.data.storage

import io.reactivex.Observable
import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.observe
import smarthome.raspberry.util.persistence.set

class LocalStorageImpl(private val storage: StorageHelper): LocalStorage {
    
    override suspend fun saveHome(homeId: String) {
        storage.set(HOME_ID, homeId)
    }
    
    override fun getHomeId(): Observable<String> = storage.observe(HOME_ID)
    
    companion object {
        private const val HOME_ID = "HOME_ID"
    }
}