package smarthome.client.presentation.scripts.addition.graph

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.controllers.epoxy.ControllerView
import smarthome.client.presentation.scripts.addition.graph.views.GraphControllerView

class ControllerBlockFactoryImpl : GraphBlockFactory {
    override fun inflate(into: ViewGroup, block: GraphBlock): GraphDraggable {
        return GraphControllerView(into.context).apply {
            into.addView(this)
        }
    }
}
