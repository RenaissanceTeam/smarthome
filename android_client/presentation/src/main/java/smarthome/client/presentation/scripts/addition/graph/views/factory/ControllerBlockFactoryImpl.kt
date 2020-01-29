package smarthome.client.presentation.scripts.addition.graph.views.factory

import android.view.ViewGroup
import smarthome.client.presentation.scripts.addition.graph.GraphDraggable
import smarthome.client.presentation.scripts.addition.graph.views.GraphControllerView
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock

class ControllerBlockFactoryImpl :
    GraphBlockFactory {
    override fun inflate(into: ViewGroup, block: GraphBlock): GraphDraggable {
        return GraphControllerView(
            into.context).apply {
            into.addView(this)
        }
    }
}
