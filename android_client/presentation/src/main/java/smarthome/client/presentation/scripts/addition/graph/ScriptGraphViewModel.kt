package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import smarthome.client.presentation.util.KoinViewModel

class ScriptGraphViewModel : KoinViewModel() {
    
    val blocks = MutableLiveData<MutableMap<GraphBlockIdentifier, GraphBlock>>(mutableMapOf())
    
    fun onDropped(info: DragOperationInfo, dropPosition: Position) {
        val identifier = info.blockId
        val current = blocks.value ?: mutableMapOf()
        
        val addedBlock = current[identifier] ?: GraphBlock(id = identifier, type = info.blockType)
        val blockOnNewPosition = addedBlock.copy(position = dropPosition) // todo add shift with draggable.touchPosition
        
        current[identifier] = blockOnNewPosition
        blocks.value = current
    }
}
