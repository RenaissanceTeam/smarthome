package smarthome.client.presentation.scripts.addition.graph.events.dependency

import smarthome.client.entity.script.BlockId
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position

data class DependencyEvent(
    val id: String,
    val status: String,
    val startId: BlockId,
    val endId: BlockId? = null,
    val rawEndPosition: Position
): GraphEvent