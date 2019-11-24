package smarthome.raspberry.home.api.domain

import io.reactivex.Observable

interface GetHomeIdUseCase {
    fun execute(): Observable<String>
}