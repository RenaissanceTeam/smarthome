package smarthome.client.data.api.homeserver

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Observable
import smarthome.client.entity.HomeServer

@Dao
interface HomeServerRepo {
    @Query("select * from HomeServer")
    fun get(): Observable<List<HomeServer>>
    
    @Query("select * from HomeServer where active = 1" )
    suspend fun getCurrentActive(): HomeServer?

    @Query("select * from HomeServer where url = :url")
    suspend fun getByUrl(url: String): HomeServer?
    
    @Insert
    suspend fun save(homeServer: HomeServer)
    
    @Update
    suspend fun update(s: HomeServer)
}