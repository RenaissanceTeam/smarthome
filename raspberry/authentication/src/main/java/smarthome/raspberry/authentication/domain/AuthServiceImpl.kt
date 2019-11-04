package smarthome.raspberry.authentication.domain

import io.reactivex.Observable
import smarthome.raspberry.authentication_api.data.AuthRepo
import smarthome.raspberry.authentication_api.domain.AuthService
import smarthome.raspberry.authentication_api.domain.AuthStatus

class AuthServiceImpl(private val repo: AuthRepo): AuthService {
    override fun isAuthenticated(): Observable<AuthStatus> {
        return repo.getAuthStatus()
    }
}