package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import smarthome.client.presentation.scripts.addition.graph.view.GraphView
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyArrowView

class DependencyArrowFactoryImpl(private val graphView: GraphView) : DependencyArrowFactory {
    override fun create(): DependencyArrowView {
        return DependencyArrowView(graphView.context).apply {
            graphView.addView(this)
        }
    }
}