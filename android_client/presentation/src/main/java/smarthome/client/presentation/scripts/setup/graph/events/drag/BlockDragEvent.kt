package smarthome.client.presentation.scripts.setup.graph.events.drag

import smarthome.client.entity.script.block.Block
import smarthome.client.presentation.scripts.setup.graph.events.GraphEvent
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition

data class BlockDragInfo(
    val block: Block,
    val status: String,
    val from: String = UNKNOWN,
    val to: String = UNKNOWN,
    val dragTouch: Position = emptyPosition,
    val position: Position = emptyPosition
): GraphEvent {
    fun isTo(destination: String): Boolean {
        return to == destination
    }
    
    fun isFrom(destination: String): Boolean {
        return from == destination
    }
    
    fun isFromOrTo(destination: String): Boolean {
        return isFrom(destination) or (isTo(destination))
    }
}