package smarthome.client.data.api.homeserver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import smarthome.client.entity.HomeServer

@Dao
interface HomeServerRepo {
    @Query("select * from HomeServer")
    fun get(): Observable<List<HomeServer>>
    @Insert
    fun save(homeServer: HomeServer)
}