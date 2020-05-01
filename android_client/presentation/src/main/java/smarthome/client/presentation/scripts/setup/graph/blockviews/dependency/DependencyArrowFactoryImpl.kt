package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import smarthome.client.presentation.scripts.setup.graph.view.GraphView

class DependencyArrowFactoryImpl(private val graphView: GraphView) : DependencyArrowFactory {
    override fun create(): DependencyArrowView {
        return DependencyArrowView(graphView.context).apply {
            graphView.addView(this)
        }
    }
}