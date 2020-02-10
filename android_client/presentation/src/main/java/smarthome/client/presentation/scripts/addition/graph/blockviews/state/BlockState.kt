package smarthome.client.presentation.scripts.addition.graph.blockviews.state

import smarthome.client.entity.script.block.Block

interface BlockState {
    val block: Block
    val visible: Boolean
    val border: BorderStatus
    
    fun copyWithInfo(
        block: Block = this.block,
        visible: Boolean = this.visible,
        border: BorderStatus = this.border): BlockState
}