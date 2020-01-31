package smarthome.client.presentation.scripts.addition.graph.events.drag

data class ControllerDragEvent(val id: Long,
                               override val dragInfo: CommonDragInfo) : GraphDragEvent {
    
    override fun copyWithDragInfo(dragInfo: CommonDragInfo): ControllerDragEvent {
        return copy(dragInfo = dragInfo)
    }
}