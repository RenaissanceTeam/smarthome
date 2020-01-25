package smarthome.client.presentation.scripts.addition.graph

import androidx.lifecycle.MutableLiveData
import smarthome.client.presentation.replaceOrAdd
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.log

class ScriptGraphViewModel : KoinViewModel() {
    
    val blocks = MutableLiveData<MutableList<GraphBlock>>(mutableListOf())
    
    fun onDropped(draggable: GraphDraggable, dropPosition: Position) {
        log("drop at $dropPosition")
        val current = blocks.value ?: mutableListOf()
        val graphBlock = draggable.getBlock().copy(position = dropPosition)
        current.replaceOrAdd(graphBlock) { it.id == graphBlock.id }
        log("graph block = $graphBlock, all current = $current")
        
        blocks.value = current
    }
}