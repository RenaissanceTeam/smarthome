package smarthome.raspberry.authentication_api.domain

import io.reactivex.Observable
import smarthome.raspberry.authentication_api.data.AuthRepo
import smarthome.raspberry.authentication_api.domain.AuthStatus

interface AuthService {
    fun isAuthenticated(): Observable<AuthStatus>
}