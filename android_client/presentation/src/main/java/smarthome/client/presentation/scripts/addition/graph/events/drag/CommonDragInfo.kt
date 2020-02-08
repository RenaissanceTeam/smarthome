package smarthome.client.presentation.scripts.addition.graph.events.drag

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.emptyPosition

data class CommonDragInfo(
    val id: BlockId,
    val status: String,
    val from: String = UNKNOWN,
    val to: String = UNKNOWN,
    val dragTouch: Position = emptyPosition,
    val position: Position = emptyPosition
)