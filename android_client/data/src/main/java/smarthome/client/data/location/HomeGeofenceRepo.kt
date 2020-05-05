package smarthome.client.data.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import smarthome.client.entity.location.HomeGeofence

@Dao
interface HomeGeofenceRepository {

    @Insert
    suspend fun save(homeGeofence: HomeGeofence)

    @Update
    suspend fun update(homeGeofence: HomeGeofence)

    @Query("select * from HomeGeofence")
    suspend fun get(): HomeGeofence?
}