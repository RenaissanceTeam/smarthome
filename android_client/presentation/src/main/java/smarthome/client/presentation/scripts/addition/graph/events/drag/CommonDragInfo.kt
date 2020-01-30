package smarthome.client.presentation.scripts.addition.graph.events.drag

import smarthome.client.presentation.util.Position
import smarthome.client.presentation.util.emptyPosition

data class CommonDragInfo(
    val status: String,
    val from: String = UNKNOWN,
    val to: String = UNKNOWN,
    val dragTouch: Position = emptyPosition,
    val position: Position = emptyPosition
)