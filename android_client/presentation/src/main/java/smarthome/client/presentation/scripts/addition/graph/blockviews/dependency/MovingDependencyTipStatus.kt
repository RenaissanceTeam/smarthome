package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.identifier.UnknownIdentifier
import smarthome.client.presentation.util.Position
import smarthome.client.presentation.util.emptyPosition

data class MovingDependencyTipStatus(
    val dependencyId: String? = null,
    val status: String = IDLE,
    val from: GraphBlockIdentifier = UnknownIdentifier,
    val rawPosition: Position = emptyPosition
)

const val IDLE = "MovingDependencyTipStatus_IDLE"
const val MOVING = "MovingDependencyTipStatus_MOVING"
const val DROPPED = "MovingDependencyTipStatus_DROPPED"