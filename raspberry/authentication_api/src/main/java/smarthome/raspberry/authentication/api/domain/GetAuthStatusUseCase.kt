package smarthome.raspberry.authentication.api.domain

import io.reactivex.Observable

interface GetAuthStatusUseCase {
    fun execute(): Observable<AuthStatus>
}

