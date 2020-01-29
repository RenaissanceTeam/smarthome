package smarthome.client.presentation.scripts.addition.graph.events.drag

import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent

interface GraphDragEvent : GraphEvent {
    val dragInfo: CommonDragInfo
    
    fun copyWithDragInfo(dragInfo: CommonDragInfo): GraphDragEvent
}