package smarthome.client.presentation.scripts.setup.graph.eventhandler

import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragInfo

interface DragBlockEventsHandler {
    fun handle(event: BlockDragInfo)
}
