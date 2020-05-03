package smarthome.client.entity.script.block

import smarthome.client.util.Position

data class TimeBlock(
        override val id: String,
        override val position: Position
) : Block {
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}