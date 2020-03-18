package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.GraphControllerView
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState

class ControllerBlockFactoryImpl : GraphBlockFactory {
    override fun inflate(into: ViewGroup, blockState: BlockState): GraphBlockView {
        if (blockState !is ControllerBlockState) throw IllegalStateException(
            "Block factory resolved incorrectly! Block for controller factory is $blockState")
        
        return GraphControllerView(into.context).apply { into.addView(this) }
    }
}
