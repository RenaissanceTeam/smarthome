package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import smarthome.client.presentation.util.KoinViewModel

class ScriptGraphViewModel : KoinViewModel() {
    
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
    val hiddenBlocks = MutableLiveData<MutableMap<GraphBlockIdentifier, Boolean>>()
    
    fun onDropped(info: DragOperationInfo, dropPosition: Position) {
        val identifier = info.blockId
        val current = blocks.value ?: mutableMapOf()
        
        val addedBlock = current[identifier] ?: GraphBlock(id = identifier, type = info.blockType)
        val blockOnNewPosition = addedBlock.copy(position = dropPosition) // todo add shift with draggable.touchPosition
        
        current[identifier] = blockOnNewPosition
        blocks.value = current
    }
    
    fun onDragStarted(id: GraphBlockIdentifier, dragTouch: Position): DragOperationInfo {
        (hiddenBlocks.value ?: mutableMapOf())[id] = true
        
        triggerDevicesRebuildModels()
        return DragOperationInfo("controllersHub", dragTouch, "controller",
            ControllerGraphBlockIdentifier(id)) { droppedTo ->
            when (droppedTo) {
                "controllersHub" -> {
                    onDragCancelled(id)
                    true
                }
                else -> false
            }
        }
    }
}
