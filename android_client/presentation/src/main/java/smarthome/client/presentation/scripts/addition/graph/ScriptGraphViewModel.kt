package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.Position

class ScriptGraphViewModel : KoinViewModel() {
    
    private val eventBus: GraphEventBus by inject()
    private val blockResolver: GraphBlockResolver by inject()
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
    val dependencies = MutableLiveData<MutableMap<String, DependencyState>>(mutableMapOf())
    
    init {
        val dragBlockHandler = DragBlockHandler(
            emitBlocks = { blocks.value = it },
            getBlockForEvent = ::getBlockForEvent,
            getCurrentBlocks = ::getCurrentBlocks
        )
        
        val dependencyEventsHandler = DependencyEventsHandler(
            emitDependencies = { dependencies.value = it },
            getCurrentDependencies = ::getCurrentDependencies
        )
        
        disposable.add(eventBus.observe()
            .subscribe {
                if (it is GraphDragEvent) dragBlockHandler.handle(it)
                if (it is DependencyEvent) dependencyEventsHandler.handle(it)
        })
    }
    
    private fun getCurrentBlocks(): MutableMap<GraphBlockIdentifier, GraphBlock> {
        return blocks.value ?: mutableMapOf()
    }
    
    private fun getCurrentDependencies(): MutableMap<String, DependencyState> {
        return dependencies.value ?: mutableMapOf()
    }
    
    private fun getBlockForEvent(event: GraphDragEvent): GraphBlock {
        val current = getCurrentBlocks()
        val identifier = blockResolver.resolveIdentifier(event)
        
        return current[identifier] ?: blockResolver.createBlock(event)
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

