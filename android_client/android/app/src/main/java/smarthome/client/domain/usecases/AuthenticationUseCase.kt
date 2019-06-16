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

    fun onAuthFail() {
        repository.setAuthenticationStatus(false)
        repository.resetEmail()
    }

    fun onAuthSuccess() {
        repository.setAuthenticationStatus(true)
        repository.updateEmail()
    }

    fun signOut() {
        repository.setAuthenticationStatus(false)
        repository.resetEmail()
    }
}
