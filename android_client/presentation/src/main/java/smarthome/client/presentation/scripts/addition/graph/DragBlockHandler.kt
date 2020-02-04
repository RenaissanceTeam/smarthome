package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier

class DragBlockHandler(
    private val getCurrentBlocks: () -> MutableMap<GraphBlockIdentifier, GraphBlock>,
    private val emitBlocks: (MutableMap<GraphBlockIdentifier, GraphBlock>) -> Unit
) {
    fun handle(event: GraphDragEvent) {
        if (!event.isFromOrTo(GRAPH)) return
        
        when (event.dragInfo.status) {
            DRAG_DROP -> {
                if (event.isTo(GRAPH)) handleDropToGraph(event)
                if (event.isFrom(GRAPH) && !event.isTo(GRAPH)) handleBlockRemove(event)
            }
            DRAG_START -> {
                if (event.isFrom(GRAPH)) handleDragStartFromGraph(event)
            }
            DRAG_CANCEL -> {
                if (event.isFrom(GRAPH)) makeBlockVisible(event)
            }
        }
    }
    
    private fun makeBlockVisible(event: GraphDragEvent) {
        val block = getBlockForEvent(event) ?: return
        
        emitWithBlock(block.copyWithInfo(visible = true))
    }
    
    private fun handleBlockRemove(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event) ?: return
        
        val current = getCurrentBlocks()
        current.remove(blockBeforeEvent.id)
    
        emitBlocks(current)
    }
    
    private fun handleDropToGraph(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event) ?: return
        val droppedBlock = blockBeforeEvent.copyWithInfo(
            position = event.dragInfo.position,
            visible = true
        )
        
        emitWithBlock(droppedBlock)
    }
    
    private fun handleDragStartFromGraph(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event) ?: return
        val hiddenBlock = blockBeforeEvent.copyWithInfo(visible = false)
        
        emitWithBlock(hiddenBlock)
    }
    
    private fun getBlockForEvent(event: GraphDragEvent): GraphBlock? {
        return getCurrentBlocks()[event.dragInfo.id]
    }
    
    private fun emitWithBlock(block: GraphBlock) {
        val current = getCurrentBlocks()
        val id = block.id
        current[id] = block
    
        emitBlocks(current)
    }
}