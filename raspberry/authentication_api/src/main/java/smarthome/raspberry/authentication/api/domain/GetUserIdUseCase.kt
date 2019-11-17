package smarthome.raspberry.authentication.api.domain

import io.reactivex.Observable

interface GetUserIdUseCase {
    fun execute(): Observable<String>
}