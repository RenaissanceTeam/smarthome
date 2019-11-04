package smarthome.raspberry.authentication_api.data

import io.reactivex.Observable
import smarthome.raspberry.domain.usecases.AuthUseCase

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthUseCase.AuthStatus>
    fun getUserId(): Observable<String>
}