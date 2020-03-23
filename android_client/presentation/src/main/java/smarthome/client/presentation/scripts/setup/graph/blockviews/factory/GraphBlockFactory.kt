package smarthome.client.presentation.scripts.setup.graph.blockviews.factory

import android.view.ViewGroup
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

interface GraphBlockFactory {
    fun inflate(into: ViewGroup, blockState: BlockState): GraphBlockView
}