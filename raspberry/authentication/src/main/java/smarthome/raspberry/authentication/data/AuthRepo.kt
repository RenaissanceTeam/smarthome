package smarthome.raspberry.authentication.data

import com.google.firebase.auth.AuthCredential
import io.reactivex.Observable
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.User

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthStatus>
    fun getUserId(): Observable<String>
    fun getUser(): User
    suspend fun signIn(credential: AuthCredential): User
    suspend fun signOut()
}