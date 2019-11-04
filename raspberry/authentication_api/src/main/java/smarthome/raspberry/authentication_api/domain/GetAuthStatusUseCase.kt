package smarthome.raspberry.authentication_api.domain

import io.reactivex.Observable

interface GetAuthStatusUseCase {
    fun execute(): Observable<AuthStatus>
}

