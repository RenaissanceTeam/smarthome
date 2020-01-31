package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier

class DragBlockHandler(
    private val getCurrentBlocks: () -> MutableMap<GraphBlockIdentifier, GraphBlock>,
    private val getBlockForEvent: (GraphDragEvent) -> GraphBlock,
    private val emitBlocks: (MutableMap<GraphBlockIdentifier, GraphBlock>) -> Unit
) {
    fun handle(event: GraphDragEvent) {
        if (!event.isFromOrTo(
                GRAPH)) return
        
        when (event.dragInfo.status) {
            DRAG_DROP -> {
                if (event.isTo(
                        GRAPH)) handleDropToGraph(event)
                if (event.isFrom(
                        GRAPH) && !event.isTo(
                        GRAPH)) handleBlockRemove(event)
            }
            DRAG_START -> {
                if (event.isFrom(
                        GRAPH)) handleDragStartFromGraph(event)
            }
        }
    }
    
    private fun handleBlockRemove(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event)
        
        val current = getCurrentBlocks()
        current.remove(blockBeforeEvent.id)
    
        emitBlocks(current)
    }
    
    private fun handleDropToGraph(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event)
        val droppedBlock = blockBeforeEvent.copyWithInfo(
            position = event.dragInfo.position,
            visible = true
        )
        
        emitWithBlock(droppedBlock)
    }
    
    private fun handleDragStartFromGraph(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event)
        val hiddenBlock = blockBeforeEvent.copyWithInfo(visible = false)
        
        emitWithBlock(hiddenBlock)
    }
    
    private fun emitWithBlock(block: GraphBlock) {
        val current = getCurrentBlocks()
        val id = block.id
        current[id] = block
    
        emitBlocks(current)
    }
}