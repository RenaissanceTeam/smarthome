package smarthome.client.presentation.scripts.setup.graph.blockviews.time

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.TimeBlock
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BorderStatus

data class TimeBlockState(
        override val block: TimeBlock,
        override val visible: Boolean = true,
        override val border: BorderStatus = BorderStatus()
) : BlockState {

    override fun copyWithInfo(block: Block, visible: Boolean, border: BorderStatus): BlockState {
        require(block is TimeBlock)
        return copy(block = block, visible = visible, border = border)
    }
}