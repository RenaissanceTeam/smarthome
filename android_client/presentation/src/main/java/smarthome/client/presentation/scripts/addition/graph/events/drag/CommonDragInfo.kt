package smarthome.client.presentation.scripts.addition.graph.events.drag

import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.emptyPosition

data class CommonDragInfo(
    val status: String,
    val from: String = UNKNOWN,
    val to: String = UNKNOWN,
    val dragTouch: Position = emptyPosition,
    val position: Position = emptyPosition
)