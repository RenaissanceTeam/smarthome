package smarthome.client.data.api.homeserver

import androidx.room.Dao
import io.reactivex.Observable
import smarthome.client.entity.HomeServer

@Dao
interface HomeServerRepo {
    fun get(): Observable<List<HomeServer>>
    fun save(homeServer: HomeServer)
}