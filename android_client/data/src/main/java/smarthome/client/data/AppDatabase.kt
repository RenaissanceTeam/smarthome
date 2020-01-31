package smarthome.client.data

import androidx.room.Database
import androidx.room.RoomDatabase
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.entity.HomeServer
import smarthome.client.entity.User

@Database(entities = [User::class, HomeServer::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeServerRepo(): HomeServerRepo
    abstract fun userRepo(): UserRepository
}