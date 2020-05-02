package smarthome.client.presentation.scripts.setup.graph.blockviews.controller

import android.view.ViewGroup
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.factory.GraphBlockFactory
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class ControllerBlockFactory : GraphBlockFactory {
    override fun inflate(into: ViewGroup, blockState: BlockState): GraphBlockView {
        if (blockState !is ControllerBlockState) throw IllegalStateException(
            "Block factory resolved incorrectly! Block for controller factory is $blockState")
        
        return GraphControllerView(into.context).apply { into.addView(this) }
    }
}
