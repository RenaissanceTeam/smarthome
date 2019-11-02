package smarthome.raspberry.authentication_api

import io.reactivex.Observable
import smarthome.raspberry.domain.usecases.AuthUseCase

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthUseCase.AuthStatus>
    fun getUserId(): Observable<String>
}