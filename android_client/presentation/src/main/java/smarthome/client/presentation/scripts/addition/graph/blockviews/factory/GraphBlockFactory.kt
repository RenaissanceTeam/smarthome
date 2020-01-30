package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock

interface GraphBlockFactory {
    fun inflate(into: ViewGroup, block: GraphBlock): GraphBlockView
}