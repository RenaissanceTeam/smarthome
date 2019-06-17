package smarthome.client.domain

import io.reactivex.Observable

interface AuthenticationRepository {
    fun getAuthenticationStatus(): Observable<Boolean>
    fun getEmail(): Observable<String>
    suspend fun setAuthenticationStatus(isAuthenticated: Boolean)
    suspend fun resetEmail()
    suspend fun updateEmail()
    suspend fun getUserId(): String
}