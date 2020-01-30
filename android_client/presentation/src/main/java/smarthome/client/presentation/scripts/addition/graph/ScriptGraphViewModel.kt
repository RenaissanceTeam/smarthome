package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.Position

class ScriptGraphViewModel : KoinViewModel() {
    
    private val eventBus: GraphEventBus by inject()
    private val blockResolver: GraphBlockResolver by inject()
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
    
    init {
        disposable.add(eventBus.observe()
            .subscribe {
                if (it is GraphDragEvent) handleDragEvent(it)
        })
    }
    
    private fun handleDragEvent(event: GraphDragEvent) {
        if (!event.isFromOrTo(GRAPH)) return
        
        when (event.dragInfo.status) {
            DRAG_DROP -> {
                if (event.isTo(GRAPH)) handleDropToGraph(event)
                if (event.isFrom(GRAPH) && !event.isTo(GRAPH)) handleBlockRemove(event)
            }
            DRAG_START -> {
                if (event.isFrom(GRAPH)) handleDragStartFromGraph(event)
            }
        }
    }
    
    private fun handleBlockRemove(event: GraphDragEvent) {
        val blockBeforeEvent = getBlockForEvent(event)
    
        val current = getCurrentBlocks()
        current.remove(blockBeforeEvent.id)
    
        blocks.value = current
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
    
    private fun getCurrentBlocks(): MutableMap<GraphBlockIdentifier, GraphBlock> {
        return blocks.value ?: mutableMapOf()
    }
    
    private fun getBlockForEvent(event: GraphDragEvent): GraphBlock {
        val current = getCurrentBlocks()
        val identifier = blockResolver.resolveIdentifier(event)
        
        return current[identifier] ?: blockResolver.createBlock(event)
    }
    
    private fun emitWithBlock(block: GraphBlock) {
        val current = getCurrentBlocks()
        val id = block.id
        current[id] = block
        
        blocks.value = current
    }
    
    fun onDropped(event: GraphDragEvent, dropPosition: Position) {
        val droppedInfo = event.dragInfo.copy(
            status = DRAG_DROP,
            to = GRAPH,
            position = dropPosition - event.dragInfo.dragTouch
        )
        val dropEvent = event.copyWithDragInfo(droppedInfo)
        
        eventBus.addEvent(dropEvent)
    }
}
