package smarthome.raspberry.authentication.data

import io.reactivex.Observable
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthStatus>
    fun getUserId(): Observable<String>
    fun getUser(): User
    fun hasUser(): Boolean
    suspend fun signIn(credential: Credentials): User
    suspend fun signOut()
}