package smarthome.client.presentation.scripts.addition.graph

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.graph.controllers.epoxy.ControllerView

class ControllerBlockFactoryImpl : GraphBlockFactory {
    override fun inflate(into: ViewGroup, block: GraphBlock): GraphDraggable {
        return ControllerView(into.context).apply {
            controllerId = block.id
            into.addView(this)
        }
    }
}
