package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import smarthome.client.presentation.scripts.addition.graph.ScriptGraphView
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyArrowView

class DependencyArrowFactoryImpl(private val graphView: ScriptGraphView) : DependencyArrowFactory {
    override fun create(): DependencyArrowView {
        return DependencyArrowView(graphView.context).apply {
            graphView.addView(this)
        }
    }
}