package smarthome.client.entity.script.block

import smarthome.client.util.Position

interface Block {
    val id: String
    val position: Position
    fun copyWithPosition(newPosition: Position): Block
}