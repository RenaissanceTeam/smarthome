package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import smarthome.client.util.Position

data class MovingDependency(
    val id: String? = null,
    val startBlock: String? = null,
    val status: String = IDLE,
    val rawEndPosition: Position? = null
)
