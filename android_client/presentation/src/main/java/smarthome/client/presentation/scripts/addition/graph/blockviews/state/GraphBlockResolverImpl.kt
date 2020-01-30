package smarthome.client.presentation.scripts.addition.graph.blockviews.state

import smarthome.client.presentation.scripts.addition.graph.identifier.ControllerGraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

class GraphBlockResolverImpl : GraphBlockResolver {

    override fun resolveIdentifier(event: GraphDragEvent): GraphBlockIdentifier {
        return when (event) {
            is ControllerDragEvent -> ControllerGraphBlockIdentifier(
                event.id)
            else -> throw IllegalStateException("No identifier associated for event $event")
        }
    }
    
    override fun createBlock(event: GraphDragEvent): GraphBlock {
        return when (event) {
            is ControllerDragEvent -> ControllerBlock(
                ControllerGraphBlockIdentifier(
                    event.id),
                event.dragInfo.position
            )
            else -> throw IllegalStateException("No graph block associated for event $event")
        }
    }
}