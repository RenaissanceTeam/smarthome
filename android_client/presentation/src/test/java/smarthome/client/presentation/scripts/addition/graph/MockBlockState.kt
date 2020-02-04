package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.util.Position

data class MockBlockState(
    override val id: MockBlockId,
    override val position: Position,
    override val visible: Boolean = true,
    override val border: BorderStatus = BorderStatus()
) : BlockState {
    
    override fun copyWithInfo(position: Position, visible: Boolean,
                              border: BorderStatus): BlockState {
        return copy(position = position, visible = visible,
            border = border)
    }
    
}