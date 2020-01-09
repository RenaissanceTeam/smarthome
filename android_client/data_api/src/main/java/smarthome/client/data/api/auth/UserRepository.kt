package smarthome.client.data.api.auth

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Observable
import smarthome.client.entity.User

@Dao
interface UserRepository {
    @Query("select * from user")
    fun get(): Observable<User>
    
    fun delete()
}