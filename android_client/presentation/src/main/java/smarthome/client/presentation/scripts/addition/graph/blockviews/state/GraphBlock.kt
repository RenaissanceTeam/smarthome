package smarthome.client.presentation.scripts.addition.graph.blockviews.state

import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position

interface GraphBlock {
    val id: GraphBlockIdentifier
    val position: Position
    val visible: Boolean
    
    fun copyWithInfo(position: Position = this.position,
                     visible: Boolean = this.visible): GraphBlock
}