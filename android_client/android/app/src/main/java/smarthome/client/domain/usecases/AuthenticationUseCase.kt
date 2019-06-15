package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.AuthenticationRepository

class AuthenticationUseCase(private val repository: AuthenticationRepository) {
    fun getAuthenticationStatus(): Observable<Boolean> {
        return repository.getAuthenticationStatus()
    }

    fun onAuthFail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getEmail(): Observable<String> {
        TODO()
    }

    fun signOut() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onAuthSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}