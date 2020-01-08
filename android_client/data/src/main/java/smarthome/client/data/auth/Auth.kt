package smarthome.client.data.auth

import io.reactivex.Observable
import smarthome.client.data.api.auth.AuthenticationRepository

class AuthenticationRepositoryImpl: AuthenticationRepository {
    
    override fun getAuthenticationStatus(): Observable<Boolean> {
        return Observable.just(true)
    }
    
    override fun getEmail(): Observable<String> {
        return Observable.just("email")
    }
    
    override suspend fun setAuthenticationStatus(isAuthenticated: Boolean) {

    }
    
    override suspend fun resetEmail() {

    }
    
    override suspend fun updateEmail() {

    }
    
    override suspend fun getUserId(): String? {
        return "userId"
    }
}