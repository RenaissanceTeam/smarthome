package smarthome.client.presentation.scripts.addition.graph.blockviews.state

import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

interface GraphBlockResolver {
    fun resolveIdentifier(event: GraphDragEvent): GraphBlockIdentifier
    fun createBlock(event: GraphDragEvent): GraphBlock
}