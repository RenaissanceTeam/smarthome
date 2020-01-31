package smarthome.client.presentation.scripts.addition.graph.views.state

import smarthome.client.presentation.scripts.addition.graph.ControllerGraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.Position

data class ControllerBlock(
    override val id: ControllerGraphBlockIdentifier,
    override val position: Position,
    override val visible: Boolean = true
): GraphBlock {
    
    override fun copyWithInfo(position: Position, visible: Boolean): GraphBlock {
        return copy(position = position, visible = visible)
    }
}