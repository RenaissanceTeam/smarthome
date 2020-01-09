package smarthome.client.data.auth

import io.reactivex.Observable
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.entity.User

class UserRepositoryImpl : UserRepository {
    
    override fun get(): Observable<List<User>> {
        TODO()
    }
    
    override fun delete() {
        TODO()
    }
}