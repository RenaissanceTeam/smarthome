package smarthome.raspberry.home.domain.eventbus

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.eventbus.events.Event
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.data.EventBusRepository

class ObserveHomeEventsUseCaseImpl(
    private val eventBusRepository: EventBusRepository
) : ObserveHomeEventsUseCase {
    override fun execute(): Observable<Event> {
        return eventBusRepository.getEvents()
    }
}
