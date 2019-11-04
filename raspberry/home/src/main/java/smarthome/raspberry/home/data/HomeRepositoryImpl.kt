package smarthome.raspberry.home.data

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.home.data.storage.LocalStorage
import smarthome.raspberry.home.data.storage.RemoteStorage
import smarthome.raspberry.home_api.data.HomeRepository

class HomeRepositoryImpl(
        private val localStorage: LocalStorage,
        private val remoteStorage: RemoteStorage
) : HomeRepository {
    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        return remoteStorage.isHomeIdUnique(homeId)
    }

    override fun getHomeInfo(userId: Observable<String>, homeId: Observable<String>): Observable<HomeInfo> {
        return Observables.combineLatest(userId, homeId, ::HomeInfo)
    }

    override suspend fun saveHome(homeId: String) {
        localStorage.saveHome(homeId)
        remoteStorage.saveHome(homeId)
    }
}