package smarthome.client.domain.api.usecase

import io.reactivex.Observable

interface AuthenticationUseCase {
    fun getAuthenticationStatus(): Observable<Boolean>
    fun getUsername(): Observable<String>
    fun signOut()
}