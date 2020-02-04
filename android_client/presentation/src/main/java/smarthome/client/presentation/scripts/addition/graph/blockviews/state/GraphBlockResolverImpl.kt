package smarthome.client.presentation.scripts.addition.graph.blockviews.state

import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.ControllerGraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier

class GraphBlockResolverImpl : GraphBlockResolver {

    override fun resolveIdentifier(event: GraphDragEvent): GraphBlockIdentifier {
        return when (event) {
            is ControllerDragEvent -> ControllerGraphBlockIdentifier(
                event.id)
            else -> throw IllegalStateException("No identifier associated for event $event")
        }
    }
    
    override fun createBlock(event: GraphDragEvent): BlockState {
        return when (event) {
            is ControllerDragEvent -> ControllerBlockState(
                block = ControllerBlock(
                    event.id,
                    event.dragInfo.position
                )
            )
            else -> throw IllegalStateException("No graph block associated for event $event")
        }
    }
}