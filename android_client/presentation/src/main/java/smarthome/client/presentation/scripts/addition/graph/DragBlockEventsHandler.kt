package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

interface DragBlockEventsHandler {
    fun handle(event: GraphDragEvent)
}
