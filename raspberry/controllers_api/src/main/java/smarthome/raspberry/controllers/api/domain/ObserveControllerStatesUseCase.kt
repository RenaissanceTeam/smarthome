package smarthome.raspberry.controllers.api.domain

import io.reactivex.Observable

interface ObserveControllerStatesUseCase {
    fun execute(id: Long): Observable<String>
}