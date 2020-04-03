package smarthome.client.arduino.entity.block

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition

data class ArduinoControllerBlock(
        override val id: String,
        override val controllerId: Long,
        val type: String,
        override val position: Position = emptyPosition
) : ControllerBlock {

    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}