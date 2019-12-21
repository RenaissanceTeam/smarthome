package smarthome.raspberry.home.data

import io.reactivex.Observable
import smarthome.raspberry.entity.HomeInfo

interface HomeRepository {
    suspend fun isHomeIdUnique(homeId: String): Boolean
    fun hasHomeId(): Boolean
    fun getHomeInfo(userId: Observable<String>): Observable<HomeInfo>
    suspend fun saveHome(homeId: String)
}
