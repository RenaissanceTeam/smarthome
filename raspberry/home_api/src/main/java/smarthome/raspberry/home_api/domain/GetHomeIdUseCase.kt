package smarthome.raspberry.home_api.domain

import io.reactivex.Observable

interface GetHomeIdUseCase {
    fun execute(): Observable<String>
}