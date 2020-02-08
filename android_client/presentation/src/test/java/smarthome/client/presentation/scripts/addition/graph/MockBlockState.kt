package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.entity.script.Block
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus

data class MockBlockState(
    override val block: Block,
    override val visible: Boolean = true,
    override val border: BorderStatus = BorderStatus()
) : BlockState {
    
    override fun copyWithInfo(block: Block, visible: Boolean,
                              border: BorderStatus): BlockState {
        return copy(block = block, visible = visible,
            border = border)
    }
    
}