package smarthome.raspberry.authentication.api.data

import io.reactivex.Observable
import smarthome.raspberry.authentication.api.domain.AuthStatus

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthStatus>
    fun getUserId(): Observable<String>
}