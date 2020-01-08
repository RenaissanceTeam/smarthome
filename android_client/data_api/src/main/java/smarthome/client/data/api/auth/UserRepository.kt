package smarthome.client.data.api.auth

import io.reactivex.Observable
import smarthome.client.domain.api.entity.User

interface UserRepository {
    fun get(): Observable<User>
    fun delete()
}