package smarthome.client.arduino.entity.block

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position

data class ControllerBlock(
    val controllerId: Long,
    val type: String,
    override val position: Position
) : Block {
    
    override val id: BlockId = ControllerBlockId(controllerId)
    
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}