package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position

data class DependencyState(val id: String,
                           val startBlock: BlockId? = null,
                           val endBlock: BlockId? = null,
                           val rawEndPosition: Position? = null)