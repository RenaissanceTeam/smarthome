package smarthome.client.presentation.scripts.setup.graph.eventhandler

import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragEvent

interface DragBlockEventsHandler {
    fun handle(event: BlockDragEvent)
}
