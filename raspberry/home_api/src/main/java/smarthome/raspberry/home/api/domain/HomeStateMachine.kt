package smarthome.raspberry.home.api.domain

import smarthome.raspberry.home.api.domain.eventbus.events.Event

interface HomeStateMachine {
    fun registerEvent(event: Event)
}