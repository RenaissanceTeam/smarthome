package smarthome.client.presentation.scripts.setup.graph.blockviews.location

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.LocationBlock
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BorderStatus

data class LocationBlockState(
        override val block: LocationBlock,
        override val visible: Boolean = true,
        override val border: BorderStatus = BorderStatus()
) : BlockState {

    override fun copyWithInfo(block: Block, visible: Boolean, border: BorderStatus): BlockState {
        require(block is LocationBlock)
        return copy(block = block, visible = visible, border = border)
    }
}
