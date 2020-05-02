package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.DependencyArrowView

interface DependencyArrowFactory {
    fun create(): DependencyArrowView
}