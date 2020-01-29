package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.util.KoinViewModel

class ScriptGraphViewModel : KoinViewModel() {
    
    private val eventBus: GraphEventBus by inject()
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
//    val hiddenBlocks = MutableLiveData<MutableMap<GraphBlockIdentifier, Boolean>>()
    
    fun onDropped(event: GraphDragEvent, dropPosition: Position) {
        
        val droppedInfo = event.dragInfo.copy(
            status = DRAG_DROP,
            to = GRAPH,
            position = dropPosition - event.dragInfo.dragTouch
        )
        val dropEvent = event.copyWithDragInfo(droppedInfo)
        
        eventBus.addEvent(dropEvent)
//        val identifier = info.blockId
//        val current = blocks.value ?: mutableMapOf()
//
//        val addedBlock = current[identifier] ?: GraphBlock(id = identifier, type = info.blockType)
//        val blockOnNewPosition = addedBlock.copy(position = dropPosition) // todo add shift with info.touchPosition
//
//        current[identifier] = blockOnNewPosition
//        blocks.value = current
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
