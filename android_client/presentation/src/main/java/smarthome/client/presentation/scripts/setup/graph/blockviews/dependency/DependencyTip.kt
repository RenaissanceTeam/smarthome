package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import smarthome.client.util.Position

data class DependencyTip(
        val position: Position,
        val width: Int,
        val height: Int
) {
    val center = position + Position(width / 2, height / 2)
}