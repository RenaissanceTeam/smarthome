package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.HomeEventBusEvent
import smarthome.raspberry.home.api.domain.ObserveHomeEventsUseCase
import smarthome.raspberry.home.data.EventBusRepository

class ObserveHomeEventsUseCaseImpl(
    private val eventBusRepository: EventBusRepository
) : ObserveHomeEventsUseCase {
    override fun execute(): Observable<HomeEventBusEvent> {
        return eventBusRepository.getEvents()
    }
}
