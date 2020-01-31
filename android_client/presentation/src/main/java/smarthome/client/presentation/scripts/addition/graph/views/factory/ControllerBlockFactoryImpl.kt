package smarthome.client.presentation.scripts.addition.graph.views.factory

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.graph.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.views.GraphControllerView
import smarthome.client.presentation.scripts.addition.graph.views.state.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock

class ControllerBlockFactoryImpl : GraphBlockFactory {
    override fun inflate(into: ViewGroup, block: GraphBlock): GraphBlockView {
        if (block !is ControllerBlock) throw IllegalStateException(
            "Block factory resolved incorrectly! Block for controller factory is $block")
        
        return GraphControllerView(into.context).apply {
            into.addView(this)
        }
    }
}
