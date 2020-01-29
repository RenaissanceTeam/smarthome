package smarthome.client.presentation.scripts.addition.graph.views.state

import smarthome.client.presentation.scripts.addition.graph.ControllerGraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.Position

data class ControllerBlock(
    override val id: ControllerGraphBlockIdentifier,
    override val position: Position
): GraphBlock {
    
    override fun copyWithPosition(position: Position): ControllerBlock {
        return copy(position = position)
    }
}