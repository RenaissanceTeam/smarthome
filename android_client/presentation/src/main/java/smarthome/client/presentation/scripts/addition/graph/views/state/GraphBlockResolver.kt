package smarthome.client.presentation.scripts.addition.graph.views.state

import smarthome.client.presentation.scripts.addition.graph.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

interface GraphBlockResolver {
    fun resolveIdentifier(event: GraphDragEvent): GraphBlockIdentifier
    fun createBlock(event: GraphDragEvent): GraphBlock
}