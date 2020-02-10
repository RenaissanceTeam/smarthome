package smarthome.client.presentation.scripts.addition.graph.helper

import smarthome.client.domain.api.scripts.usecases.AddControllerBlockUseCase
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.Position
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

class AddBlockHelper(
    private val addControllerBlockUseCase: AddControllerBlockUseCase
) {
    fun resolveAddingFromEvent(scriptId: Long, event: GraphDragEvent): Block {
        return when (event) {
            is ControllerDragEvent -> addControllerBlockUseCase.execute(scriptId, event.id, event.dragInfo.position)
            else -> throw IllegalArgumentException("can't resolve how to add block for event $event")
        }
    }
}