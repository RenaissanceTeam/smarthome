package smarthome.raspberry.home.data

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.eventbus.events.Event

interface EventBusRepository {
    fun getEvents(): Observable<Event>
    fun addEvent(event: Event)
}