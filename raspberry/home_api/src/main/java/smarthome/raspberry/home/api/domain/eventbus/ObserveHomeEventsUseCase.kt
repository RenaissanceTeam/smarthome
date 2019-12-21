package smarthome.raspberry.home.api.domain.eventbus

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.eventbus.events.Event

interface ObserveHomeEventsUseCase {
    fun execute(): Observable<Event>
}