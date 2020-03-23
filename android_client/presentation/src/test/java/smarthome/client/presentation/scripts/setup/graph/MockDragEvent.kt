package smarthome.client.presentation.scripts.setup.graph

import smarthome.client.presentation.scripts.setup.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.setup.graph.events.drag.GraphDragEvent

data class MockDragEvent(private val common: CommonDragInfo) : GraphDragEvent {
    override val dragInfo: CommonDragInfo = common
    
    override fun copyWithDragInfo(dragInfo: CommonDragInfo): GraphDragEvent {
        return copy(common = dragInfo)
    }
}