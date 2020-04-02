package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.Position

data class MovingDependency(
    val id: DependencyId? = null,
    val startBlock: String? = null,
    val status: String = IDLE,
    val rawEndPosition: Position? = null
)
