package smarthome.client.presentation.scripts.addition.graph.blockviews

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.util.Position

interface GraphBlockView {
    fun setData(block: GraphBlock)
    val centerPosition: Position
}