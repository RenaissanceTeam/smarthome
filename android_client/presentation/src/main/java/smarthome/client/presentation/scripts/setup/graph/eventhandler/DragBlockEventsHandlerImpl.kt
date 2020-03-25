package smarthome.client.presentation.scripts.setup.graph.eventhandler

import smarthome.client.domain.api.scripts.usecases.setup.AddBlockUseCase
import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragInfo
import smarthome.client.presentation.scripts.setup.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.setup.graph.events.drag.GRAPH

class DragBlockEventsHandlerImpl(
    private val addBlockUseCase: AddBlockUseCase
): DragBlockEventsHandler {
    
    override fun handle(event: BlockDragInfo) {
        if (!event.isFromOrTo(GRAPH)) return
        
        when (event.status) {
            DRAG_DROP -> {
                if (event.isTo(GRAPH)) addBlockUseCase.execute(event.block)
            }
        }
    }
}