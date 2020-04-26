package smarthome.client.presentation.scripts.setup.graph.blockviews.factory

import smarthome.client.presentation.scripts.setup.graph.view.GraphView
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.DependencyArrowView

class DependencyArrowFactoryImpl(private val graphView: GraphView) : DependencyArrowFactory {
    override fun create(): DependencyArrowView {
        return DependencyArrowView(graphView.context).apply {
            graphView.addView(this)
        }
    }
}