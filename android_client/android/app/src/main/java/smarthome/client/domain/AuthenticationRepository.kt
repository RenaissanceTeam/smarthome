package smarthome.client.domain

import io.reactivex.Observable

interface AuthenticationRepository {
    fun getAuthenticationStatus(): Observable<Boolean>
}