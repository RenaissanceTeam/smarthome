package smarthome.client.data

import androidx.room.Database
import androidx.room.RoomDatabase
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.data.location.HomeGeofenceRepository
import smarthome.client.entity.HomeServer
import smarthome.client.entity.User
import smarthome.client.entity.location.HomeGeofence

@Database(
        entities = [User::class, HomeServer::class, HomeGeofence::class],
        version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeServerRepo(): HomeServerRepo
    abstract fun userRepo(): UserRepository
    abstract fun homeGeofenceRepo(): HomeGeofenceRepository
}