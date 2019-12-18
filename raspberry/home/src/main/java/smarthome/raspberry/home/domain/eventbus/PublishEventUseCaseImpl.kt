package smarthome.raspberry.home.domain.eventbus

import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.Event
import smarthome.raspberry.home.data.EventBusRepository

class PublishEventUseCaseImpl(
    private val eventBusRepository: EventBusRepository,
    private val homeStateMachine: HomeStateMachine
): PublishEventUseCase {
    override fun publish(event: Event) {
        eventBusRepository.addEvent(event)
        homeStateMachine.registerEvent(event)
    }
}