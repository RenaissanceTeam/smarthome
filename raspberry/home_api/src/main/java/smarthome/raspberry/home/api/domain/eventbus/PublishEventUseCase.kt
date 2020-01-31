package smarthome.raspberry.home.api.domain.eventbus

import smarthome.raspberry.home.api.domain.eventbus.events.Event

interface PublishEventUseCase {
    fun execute(event: Event)
}