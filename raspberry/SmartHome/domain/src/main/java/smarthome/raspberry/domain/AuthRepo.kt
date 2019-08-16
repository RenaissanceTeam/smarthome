package smarthome.raspberry.domain

import io.reactivex.Observable
import smarthome.raspberry.domain.usecases.AuthUseCase

interface AuthRepo {
    fun getAuthStatus(): Observable<AuthUseCase.AuthStatus>
}