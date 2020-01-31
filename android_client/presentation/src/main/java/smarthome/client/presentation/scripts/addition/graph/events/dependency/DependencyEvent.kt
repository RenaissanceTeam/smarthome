package smarthome.client.presentation.scripts.addition.graph.events.dependency

import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position

data class DependencyEvent(
    val status: String,
    val startId: GraphBlockIdentifier?,
    val endId: GraphBlockIdentifier? = null,
    val rawEndPosition: Position
): GraphEvent