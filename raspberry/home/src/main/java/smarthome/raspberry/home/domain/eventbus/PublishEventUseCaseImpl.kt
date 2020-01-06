package smarthome.raspberry.home.domain.eventbus

import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.Event
import smarthome.raspberry.home.data.EventBusRepository

class PublishEventUseCaseImpl(
    private val eventBusRepository: EventBusRepository
): PublishEventUseCase {
    override fun execute(event: Event) {
        eventBusRepository.addEvent(event)
    }
}