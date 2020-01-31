package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position

data class DependencyState(val id: String,
                           val startBlock: GraphBlockIdentifier? = null,
                           val endBlock: GraphBlockIdentifier? = null,
                           val endPosition: Position? = null)