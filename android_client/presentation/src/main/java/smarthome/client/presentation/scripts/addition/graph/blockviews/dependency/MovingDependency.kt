package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.Position

data class MovingDependency(
    val id: DependencyId? = null,
    val startBlock: BlockId? = null,
    val status: String = IDLE,
    val rawEndPosition: Position? = null
)
