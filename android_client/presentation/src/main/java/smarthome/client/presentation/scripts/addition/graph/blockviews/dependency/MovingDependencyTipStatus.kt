package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position
import smarthome.client.presentation.util.emptyPosition

data class MovingDependencyTipStatus(val isMoving: Boolean = true,
                                     val from: GraphBlockIdentifier,
                                     val rawPosition: Position = emptyPosition)