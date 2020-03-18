package smarthome.client.presentation.scripts.addition.graph.events.drag

data class BlockDragEvent(override val dragInfo: CommonDragInfo) : GraphDragEvent {
    
    override fun copyWithDragInfo(dragInfo: CommonDragInfo): BlockDragEvent {
        return copy(dragInfo = dragInfo)
    }
}