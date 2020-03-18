package smarthome.client.presentation.scripts.addition.graph.helper

import smarthome.client.domain.api.scripts.usecases.AddControllerBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.presentation.scripts.addition.graph.events.drag.BlockDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

class AddBlockHelper(
    private val addControllerBlockUseCase: AddControllerBlockUseCase
) {
    fun resolveAddingFromEvent(scriptId: Long, event: GraphDragEvent): Block {
        return when (event) {
            is BlockDragEvent -> resolveDragEvent(event, scriptId)
            else -> throw IllegalArgumentException("can't resolve how to add block for event $event")
        }
    }
    
    private fun resolveDragEvent(event: GraphDragEvent, scriptId: Long): Block {
        return when (val id = event.dragInfo.id) {
            is ControllerBlockId ->
                addControllerBlockUseCase.execute(scriptId, id.id, event.dragInfo.position)
            else -> throw IllegalArgumentException("can't resolve how to add dragged block with id $id")
        }
    }
}