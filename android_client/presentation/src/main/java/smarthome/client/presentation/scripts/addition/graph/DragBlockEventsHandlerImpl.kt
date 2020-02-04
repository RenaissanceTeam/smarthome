package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.domain.api.scripts.usecases.AddBlockToScriptGraphUseCase
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.domain.api.scripts.usecases.RemoveBlockUseCase
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.withReplacedOrAdded

class DragBlockEventsHandlerImpl(
    private val getCurrentBlocks: () -> List<BlockState>,
    private val emitBlocks: (List<BlockState>) -> Unit,
    private val moveBlockUseCase: MoveBlockUseCase,
    private val removeBlockUseCase: RemoveBlockUseCase,
    private val addBlockToScriptGraphUseCase: AddBlockToScriptGraphUseCase,
    private val addBlockHelper: AddBlockHelper,
    private val addGraphBlockStateHelper: AddGraphBlockStateHelper
): DragBlockEventsHandler {
    val scriptId: Long = 1L // TODO
    
    override fun handle(event: GraphDragEvent) {
        if (!event.isFromOrTo(
                GRAPH)) return
        
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
        
        removeBlockUseCase.execute(scriptId, blockBeforeEvent.block.id)
    }
    
    private fun handleDropToGraph(event: GraphDragEvent) {
        val newPosition = event.dragInfo.position
        
        val block = when (event.isFrom(GRAPH)) {
            true -> moveBlockUseCase.execute(scriptId, event.dragInfo.id, newPosition)
            false -> addBlockHelper.resolveAddingFromEvent(scriptId, event, newPosition)
        }
        val blockBeforeEvent = getBlockForEvent(event)
            ?: addGraphBlockStateHelper.createBlockState(block)
    
        val droppedBlock = blockBeforeEvent.copyWithInfo(
            block = block,
            visible = true
        )
        
        emitWithBlock(droppedBlock)
    }
    
    private fun handleDragStartFromGraph(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event) ?: return
        
        emitWithBlock(blockBeforeEvent.copyWithInfo(visible = false))
    }
    
    private fun getBlockForEvent(event: GraphDragEvent): BlockState? {
        return getCurrentBlocks().find { it.block.id == event.dragInfo.id }
    }
    
    private fun emitWithBlock(blockState: BlockState) {
        val current = getCurrentBlocks()
        val id = blockState.block.id
        
        emitBlocks(current.withReplacedOrAdded(blockState) { it.block.id == id })
    }
}