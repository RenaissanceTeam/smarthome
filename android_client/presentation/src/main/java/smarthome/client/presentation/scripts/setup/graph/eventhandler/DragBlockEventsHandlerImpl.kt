package smarthome.client.presentation.scripts.setup.graph.eventhandler

import androidx.lifecycle.MutableLiveData
import smarthome.client.domain.api.scripts.usecases.setup.MoveBlockUseCase
import smarthome.client.domain.api.scripts.usecases.setup.RemoveBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.events.drag.*
import smarthome.client.presentation.scripts.setup.graph.helper.AddBlockHelper
import smarthome.client.presentation.scripts.setup.graph.mapper.BlockToNewGraphBlockStateMapper
import smarthome.client.util.withReplacedOrAdded

class DragBlockEventsHandlerImpl(
    private val blocks: MutableLiveData<List<BlockState>>,
    private val moveBlockUseCase: MoveBlockUseCase,
    private val removeBlockUseCase: RemoveBlockUseCase,
    private val addBlockHelper: AddBlockHelper,
    private val blockToNewGraphBlockStateMapper: BlockToNewGraphBlockStateMapper
): DragBlockEventsHandler {
    
    override fun handle(event: GraphDragEvent) {
        if (!event.isFromOrTo(GRAPH)) return
        
        when (event.dragInfo.status) {
            DRAG_DROP -> {
                if (event.isTo(GRAPH)) addOrMoveBlock(event)
                if (event.isFrom(GRAPH) && !event.isTo(GRAPH)) removeBlock(event)
            }
            DRAG_START -> {
                if (event.isFrom(GRAPH)) hideDraggedBlock(event)
            }
            DRAG_CANCEL -> {
                if (event.isFrom(GRAPH)) makeBlockVisible(event)
            }
        }
    }
    
    private fun makeBlockVisible(event: GraphDragEvent) {
        val block = getBlockState(event.dragInfo.id) ?: return
        
        emitWithBlock(block.copyOfVisible())
    }
    
    private fun removeBlock(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockState(event.dragInfo.id) ?: return
    
        removeBlockUseCase.execute(blockBeforeEvent.block.id)
    }
    
    private fun addOrMoveBlock(event: GraphDragEvent) {
        when (event.isFrom(GRAPH)) {
            true -> moveBlockUseCase.execute(event.dragInfo.id, event.dragInfo.position)
            false -> addBlockHelper.resolveAddingFromEvent(event)
        }
    }
    
    private fun hideDraggedBlock(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockState(event.dragInfo.id) ?: return
        
        emitWithBlock(blockBeforeEvent.copyOfHidden())
    }
    
    private fun getBlockState(id: BlockId): BlockState? {
        return blocks.value?.find { it.block.id == id }
    }
    
    private fun emitWithBlock(blockState: BlockState) {
        val current = blocks.value.orEmpty()
        val id = blockState.block.id
        
        blocks.value = current.withReplacedOrAdded(blockState) { it.block.id == id }
    }
    
    private fun BlockState.copyOfHidden(): BlockState {
        return copyWithInfo(visible = false)
    }
    
    private fun BlockState.copyOfVisible(): BlockState {
        return copyWithInfo(visible = true)
    }
    
    private fun BlockState.copyWithDomain(block: Block): BlockState {
        return copyWithInfo(block = block)
    }
}