package smarthome.raspberry.authentication

import io.reactivex.Observable
import smarthome.raspberry.authentication_api.AuthRepo

class AuthUseCase(private val repo: smarthome.raspberry.authentication_api.AuthRepo) {
    fun isAuthenticated(): Observable<AuthStatus> {
        return repo.getAuthStatus()
    }

    enum class AuthStatus {
        SIGNED_IN, NOT_SIGNED_IN
    }
}