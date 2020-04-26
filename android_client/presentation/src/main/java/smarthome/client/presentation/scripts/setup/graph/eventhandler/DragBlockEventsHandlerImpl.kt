package smarthome.client.presentation.scripts.setup.graph.eventhandler

import smarthome.client.domain.api.scripts.usecases.setup.AddBlockUseCase
import smarthome.client.domain.api.scripts.usecases.setup.MoveBlockUseCase
import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragEvent
import smarthome.client.presentation.scripts.setup.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.setup.graph.events.drag.GRAPH

class DragBlockEventsHandlerImpl(
    private val addBlockUseCase: AddBlockUseCase,
    private val moveBlockUseCase: MoveBlockUseCase
): DragBlockEventsHandler {
    
    override fun handle(event: BlockDragEvent) {
        if (!event.isFromOrTo(GRAPH)) return
        
        when (event.status) {
            DRAG_DROP -> {
                if (event.isTo(GRAPH)) {
                    addBlockUseCase.execute(event.block)
                    moveBlockUseCase.execute(event.block.id, event.position)
                }
            }
        }
    }
}