package smarthome.client.data.auth

import io.reactivex.subjects.PublishSubject
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.entity.NOT_SIGNED_IN
import smarthome.client.entity.User

class UserRepositoryImpl : UserRepository {
    private val user = PublishSubject.create<User>()
    
    override fun get() = user
    override fun delete() {
        TODO()
    }
}