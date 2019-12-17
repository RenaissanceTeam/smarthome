package smarthome.raspberry.home.data

import io.reactivex.subjects.PublishSubject
import smarthome.raspberry.home.api.domain.HomeEventBusEvent

class EventBusRepositoryImpl : EventBusRepository {
    private val eventBus = PublishSubject.create<HomeEventBusEvent>()
    
    override fun getEvents() = eventBus
    override fun addEvent(event: HomeEventBusEvent) {
        eventBus.onNext(event)
    }
}