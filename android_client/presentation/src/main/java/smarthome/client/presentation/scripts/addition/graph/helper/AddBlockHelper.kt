package smarthome.client.presentation.scripts.addition.graph.helper

import smarthome.client.domain.api.scripts.usecases.setup.AddControllerBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.presentation.scripts.addition.graph.events.drag.BlockDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

class AddBlockHelper(
    private val addControllerBlockUseCase: AddControllerBlockUseCase
) {
    fun resolveAddingFromEvent(event: GraphDragEvent): Block {
        return when (event) {
            is BlockDragEvent -> resolveDragEvent(event)
            else -> throw IllegalArgumentException("can't resolve how to add block for event $event")
        }
    }
    
    private fun resolveDragEvent(event: GraphDragEvent): Block {
        return when (val id = event.dragInfo.id) {
            is ControllerBlockId ->
                addControllerBlockUseCase.execute(id.id, event.dragInfo.position)
            else -> throw IllegalArgumentException("can't resolve how to add dragged block with id $id")
        }
    }
}