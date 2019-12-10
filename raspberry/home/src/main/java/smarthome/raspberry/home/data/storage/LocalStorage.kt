package smarthome.raspberry.home.data.storage

import io.reactivex.Observable

interface LocalStorage {
    suspend fun saveHome(homeId: String)
    fun getHomeId(): Observable<String>
}

