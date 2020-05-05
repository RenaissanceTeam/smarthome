package smarthome.client.data.location

import androidx.room.Dao
import androidx.room.Insert
import smarthome.client.entity.location.HomeGeofence

@Dao
interface HomeGeofenceRepository {

    @Insert
    fun save(homeGeofence: HomeGeofence)
}