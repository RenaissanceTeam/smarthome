package smarthome.raspberry.authentication_api.data

import io.reactivex.Observable
import smarthome.raspberry.authentication_api.domain.AuthStatus

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthStatus>
    fun getUserId(): Observable<String>
}