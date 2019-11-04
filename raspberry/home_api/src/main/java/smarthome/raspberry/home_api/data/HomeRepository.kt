package smarthome.raspberry.home_api.data

import io.reactivex.Observable
import smarthome.raspberry.entity.HomeInfo

interface HomeRepository {
    suspend fun isHomeIdUnique(homeId: String): Boolean
    fun getHomeInfo(): Observable<HomeInfo>
    suspend fun saveHome(homeId: String)
}
