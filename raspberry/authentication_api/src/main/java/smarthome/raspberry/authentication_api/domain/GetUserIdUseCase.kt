package smarthome.raspberry.authentication_api.domain

import io.reactivex.Observable

interface GetUserIdUseCase {
    fun execute(): Observable<String>
}