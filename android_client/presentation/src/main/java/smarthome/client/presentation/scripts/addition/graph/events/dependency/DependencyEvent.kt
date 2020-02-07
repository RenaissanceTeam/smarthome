package smarthome.client.presentation.scripts.addition.graph.events.dependency

import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent

data class DependencyEvent(
    val id: DependencyId,
    val status: String,
    val startId: BlockId,
    val endId: BlockId? = null,
    val rawEndPosition: Position
): GraphEvent