package smarthome.client.presentation.scripts.setup.graph.blockviews.controller

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.ControllerBlock
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

data class ControllerBlockState(
        override val block: ControllerBlock,
        override val visible: Boolean = true,
        override val border: BorderStatus = BorderStatus()
) : BlockState {
    
    override fun copyWithInfo(block: Block, visible: Boolean, border: BorderStatus): BlockState {
        require(block is ControllerBlock)
        
        return copy(block = block, visible = visible, border = border)
    }
}