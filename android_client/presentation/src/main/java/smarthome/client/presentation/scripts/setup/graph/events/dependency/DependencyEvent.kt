package smarthome.client.presentation.scripts.setup.graph.events.dependency

import smarthome.client.presentation.scripts.setup.graph.events.GraphEvent
import smarthome.client.util.Position

data class DependencyEvent(
    val id: String,
    val status: String,
    val startId: String,
    val rawEndPosition: Position
) : GraphEvent