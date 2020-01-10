package smarthome.client.data.api.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import smarthome.client.entity.User

@Dao
interface UserRepository {
    @Query("select * from User")
    fun get(): Observable<List<User>>
    
    @Insert
    fun save(user: User)
}