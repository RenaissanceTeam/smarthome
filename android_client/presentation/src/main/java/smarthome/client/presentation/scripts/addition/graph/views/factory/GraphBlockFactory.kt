package smarthome.client.presentation.scripts.addition.graph.views.factory

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.graph.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock

interface GraphBlockFactory {
    fun inflate(into: ViewGroup, block: GraphBlock): GraphBlockView
}