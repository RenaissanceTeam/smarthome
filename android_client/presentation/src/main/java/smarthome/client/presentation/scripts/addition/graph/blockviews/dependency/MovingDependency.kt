package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.DependencyId
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.emptyPosition

data class MovingDependency(
    val id: DependencyId? = null,
    val startBlock: BlockId? = null,
    val status: String = IDLE,
    val rawEndPosition: Position? = null
)

const val IDLE = "MovingDependency_IDLE"
const val MOVING = "MovingDependency_MOVING"
const val DROPPED = "MovingDependency_DROPPED"
