package smarthome.raspberry.home.data

import io.reactivex.subjects.PublishSubject
import smarthome.raspberry.home.api.domain.eventbus.events.Event

class EventBusRepositoryImpl : EventBusRepository {
    private val eventBus = PublishSubject.create<Event>()
    
    override fun getEvents() = eventBus
    override fun addEvent(event: Event) {
        eventBus.onNext(event)
    }
}