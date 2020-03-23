package smarthome.client.presentation.scripts.setup.graph.eventhandler

import smarthome.client.presentation.scripts.setup.graph.events.drag.GraphDragEvent

interface DragBlockEventsHandler {
    fun handle(event: GraphDragEvent)
}
