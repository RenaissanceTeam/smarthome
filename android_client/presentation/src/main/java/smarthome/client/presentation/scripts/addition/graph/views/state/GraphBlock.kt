package smarthome.client.presentation.scripts.addition.graph.views.state

import smarthome.client.presentation.scripts.addition.graph.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.Position

interface GraphBlock {
    val id: GraphBlockIdentifier
    val position: Position
    val visible: Boolean
    
    fun copyWithInfo(position: Position = this.position,
                     visible: Boolean = this.visible): GraphBlock
}