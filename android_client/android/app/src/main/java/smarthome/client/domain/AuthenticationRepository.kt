package smarthome.client.domain

import io.reactivex.Observable

interface AuthenticationRepository {
    fun getAuthenticationStatus(): Observable<Boolean>
    fun getEmail(): Observable<String>
    fun setAuthenticationStatus(isAuthenticated: Boolean)
    fun resetEmail()
    fun updateEmail()
}