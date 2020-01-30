package smarthome.client.presentation.scripts.addition.graph.blockviews.state

import smarthome.client.presentation.scripts.addition.graph.identifier.ControllerGraphBlockIdentifier
import smarthome.client.presentation.util.Position

data class ControllerBlock(
    override val id: ControllerGraphBlockIdentifier,
    override val position: Position,
    override val visible: Boolean = true
): GraphBlock {
    
    override fun copyWithInfo(position: Position, visible: Boolean): GraphBlock {
        return copy(position = position, visible = visible)
    }
}