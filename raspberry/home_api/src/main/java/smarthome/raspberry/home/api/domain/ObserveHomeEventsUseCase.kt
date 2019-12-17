package smarthome.raspberry.home.api.domain

import io.reactivex.Observable

interface ObserveHomeEventsUseCase {
    fun execute(): Observable<HomeEventBusEvent>
}