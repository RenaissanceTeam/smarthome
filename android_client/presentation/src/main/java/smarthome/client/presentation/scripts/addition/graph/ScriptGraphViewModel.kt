package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlockResolver
import smarthome.client.presentation.util.KoinViewModel

class ScriptGraphViewModel : KoinViewModel() {
    
    private val eventBus: GraphEventBus by inject()
    private val blockResolver: GraphBlockResolver by inject()
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
//    val hiddenBlocks = MutableLiveData<MutableMap<GraphBlockIdentifier, Boolean>>()
    
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
                if (!event.isFrom(GRAPH) && event.isTo(GRAPH)) handleDropToGraph(event)
            }
        }
    }
    
    private fun handleDropToGraph(event: GraphDragEvent) {
        val identifier = blockResolver.resolveIdentifier(event)
        val current = blocks.value ?: mutableMapOf()

        val addedBlock = current[identifier] ?: blockResolver.createBlock(event)
        val blockOnNewPosition = addedBlock.copyWithPosition(event.dragInfo.position)
        
        current[identifier] = blockOnNewPosition
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
    
//    fun onDragStarted(id: GraphBlockIdentifier, dragTouch: Position): DragOperationInfo {
//        (hiddenBlocks.value ?: mutableMapOf())[id] = true
//
//        triggerDevicesRebuildModels()
//        return DragOperationInfo("controllersHub", dragTouch, "controller",
//            ControllerGraphBlockIdentifier(id)) { droppedTo ->
//            when (droppedTo) {
//                "controllersHub" -> {
//                    onDragCancelled(id)
//                    true
//                }
//                else -> false
//            }
//        }
//    }
}
