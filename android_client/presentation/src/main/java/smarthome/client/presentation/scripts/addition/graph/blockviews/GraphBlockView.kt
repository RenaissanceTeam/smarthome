package smarthome.client.presentation.scripts.addition.graph.blockviews

import smarthome.client.util.Position
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState

interface GraphBlockView {
    fun setData(blockState: BlockState)
    fun contains(position: Position): Boolean
    val centerPosition: Position
}