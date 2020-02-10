package smarthome.client.presentation.scripts.addition.graph.eventhandler

import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent

interface DependencyEventsHandler {
    fun handle(event: DependencyEvent)
}
