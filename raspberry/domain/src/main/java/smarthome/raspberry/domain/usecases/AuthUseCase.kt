package smarthome.raspberry.domain.usecases

import io.reactivex.Observable
import smarthome.raspberry.domain.AuthRepo

class AuthUseCase(private val repo: AuthRepo) {
    fun isAuthenticated(): Observable<AuthStatus> {
        return repo.getAuthStatus()
    }

    enum class AuthStatus {
        SIGNED_IN, NOT_SIGNED_IN
    }
}