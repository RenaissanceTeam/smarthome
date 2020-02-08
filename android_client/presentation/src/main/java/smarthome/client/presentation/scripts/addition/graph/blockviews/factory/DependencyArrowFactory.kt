package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyArrowView

interface DependencyArrowFactory {
    fun create(): DependencyArrowView
}