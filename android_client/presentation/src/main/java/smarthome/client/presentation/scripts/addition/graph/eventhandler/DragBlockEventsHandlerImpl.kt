package smarthome.client.presentation.scripts.addition.graph.eventhandler

import androidx.lifecycle.MutableLiveData
import smarthome.client.domain.api.scripts.usecases.AddBlockToScriptGraphUseCase
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.domain.api.scripts.usecases.RemoveBlockUseCase
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.presentation.scripts.addition.graph.helper.AddBlockHelper
import smarthome.client.presentation.scripts.addition.graph.helper.AddGraphBlockStateHelper
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.util.withReplacedOrAdded

class DragBlockEventsHandlerImpl(
    private val blocks: MutableLiveData<List<BlockState>>,
    private val moveBlockUseCase: MoveBlockUseCase,
    private val removeBlockUseCase: RemoveBlockUseCase,
    private val addBlockToScriptGraphUseCase: AddBlockToScriptGraphUseCase,
    private val addBlockHelper: AddBlockHelper,
    private val addGraphBlockStateHelper: AddGraphBlockStateHelper
): DragBlockEventsHandler {
    val scriptId: Long = 1L // TODO
    
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
        
        removeBlockUseCase.execute(scriptId, blockBeforeEvent.block.id)
    }
    
    private fun addOrMoveBlock(event: GraphDragEvent) {
        val block = when (event.isFrom(GRAPH)) {
            true -> moveBlockUseCase.execute(scriptId, event.dragInfo.id, event.dragInfo.position)
            false -> addBlockHelper.resolveAddingFromEvent(scriptId, event)
        }
        
        emitWithBlock(
            getOrCreateBlockState(event)
                .copyWithDomain(block)
                .copyOfVisible()
        )
    }
    
    private fun hideDraggedBlock(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockState(event.dragInfo.id) ?: return
        
        emitWithBlock(blockBeforeEvent.copyOfHidden())
    }
    
    private fun getOrCreateBlockState(event: GraphDragEvent): BlockState {
        return getBlockState(event.dragInfo.id)
            ?: addGraphBlockStateHelper.createBlockState(
                block = addBlockHelper.resolveAddingFromEvent(scriptId, event)
            )
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