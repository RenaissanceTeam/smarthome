package smarthome.client.arduino.entity.block

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.util.Position

data class ArduinoControllerBlock(
    val controllerId: Long,
    val type: String,
    override val position: Position
) : ControllerBlock {
    
    override val id = ControllerBlockId(controllerId)
    
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}