package smarthome.client.presentation.scripts.setup.graph.blockviews.notifications

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.NotificationBlock
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BorderStatus

data class NotificationBlockState(
        override val block: NotificationBlock,
        override val visible: Boolean = true,
        override val border: BorderStatus = BorderStatus()
) : BlockState {

    override fun copyWithInfo(block: Block, visible: Boolean, border: BorderStatus): BlockState {
        require(block is NotificationBlock)

        return copy(block = block, visible = visible, border = border)
    }
}