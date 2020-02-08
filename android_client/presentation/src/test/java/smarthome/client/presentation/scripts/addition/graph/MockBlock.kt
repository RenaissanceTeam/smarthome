package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position

data class MockBlock(override val id: BlockId, override val position: Position) : Block {
 
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}