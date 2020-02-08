package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent

data class MockDragEvent(private val common: CommonDragInfo) : GraphDragEvent {
    override val dragInfo: CommonDragInfo = common
    
    override fun copyWithDragInfo(dragInfo: CommonDragInfo): GraphDragEvent {
        return copy(common = dragInfo)
    }
}