package smarthome.client.presentation.scripts.addition.graph.events.drag

import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition

data class CommonDragInfo(
    val id: BlockId,
    val status: String,
    val from: String = UNKNOWN,
    val to: String = UNKNOWN,
    val dragTouch: Position = emptyPosition,
    val position: Position = emptyPosition
)