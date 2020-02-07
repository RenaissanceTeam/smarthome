package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position

data class MockBlock(override val id: BlockId, override val position: Position) : Block {
 
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}