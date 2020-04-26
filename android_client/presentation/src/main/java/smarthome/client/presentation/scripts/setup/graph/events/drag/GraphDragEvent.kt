package smarthome.client.presentation.scripts.setup.graph.events.drag

import smarthome.client.presentation.scripts.setup.graph.events.GraphEvent

interface GraphDragEvent : GraphEvent {
    val dragInfo: CommonDragInfo
    
    fun copyWithDragInfo(dragInfo: CommonDragInfo): GraphDragEvent
    
    fun isTo(destination: String): Boolean {
        return dragInfo.to == destination
    }
    
    fun isFrom(destination: String): Boolean {
        return dragInfo.from == destination
    }
    
    fun isFromOrTo(destination: String): Boolean {
        return isFrom(destination) or (isTo(destination))
    }
}