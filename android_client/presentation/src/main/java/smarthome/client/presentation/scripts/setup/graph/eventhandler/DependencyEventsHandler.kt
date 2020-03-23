package smarthome.client.presentation.scripts.setup.graph.eventhandler

import smarthome.client.presentation.scripts.setup.graph.events.dependency.DependencyEvent

interface DependencyEventsHandler {
    fun handle(event: DependencyEvent)
}
