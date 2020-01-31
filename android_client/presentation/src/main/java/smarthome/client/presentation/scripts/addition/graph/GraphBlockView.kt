package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock

interface GraphBlockView {
    fun setData(block: GraphBlock)
}