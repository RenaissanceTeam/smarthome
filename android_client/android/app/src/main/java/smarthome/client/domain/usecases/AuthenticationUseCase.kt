package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.AuthenticationRepository

class AuthenticationUseCase(private val repository: AuthenticationRepository) {
    fun getAuthenticationStatus(): Observable<Boolean> {
        return repository.getAuthenticationStatus()
    }

    fun getEmail(): Observable<String> {
        return repository.getEmail()
    }

    suspend fun onAuthFail() {
        repository.setAuthenticationStatus(false)
        repository.resetEmail()
    }

    suspend fun onAuthSuccess() {
        repository.setAuthenticationStatus(true)
        repository.updateEmail()
    }

    suspend fun signOut() {
        repository.setAuthenticationStatus(false)
        repository.resetEmail()
    }

    suspend fun getUserId(): String {
        return repository.getUserId()
    }
}
