package smarthome.client.presentation.scripts.setup.graph

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.util.Position

data class MockControllerBlock(override val id: ControllerBlockId, override val position: Position) : ControllerBlock {
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}