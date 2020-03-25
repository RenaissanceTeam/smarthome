package smarthome.client.presentation.scripts.setup.graph.events.drag

import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition

data class CommonDragInfo(
    val block: Block,
    val status: String,
    val from: String = UNKNOWN,
    val to: String = UNKNOWN,
    val dragTouch: Position = emptyPosition,
    val position: Position = emptyPosition
)