package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.emptyPosition

data class MovingDependencyTipStatus(
    val dependencyId: String? = null,
    val from: BlockId? = null,
    val rawPosition: Position = emptyPosition
)
