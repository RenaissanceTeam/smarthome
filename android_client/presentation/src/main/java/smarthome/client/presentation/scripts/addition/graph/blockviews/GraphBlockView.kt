package smarthome.client.presentation.scripts.addition.graph.blockviews

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock

interface GraphBlockView {
    fun setData(block: GraphBlock)
}