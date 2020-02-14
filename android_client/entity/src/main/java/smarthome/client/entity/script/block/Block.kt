package smarthome.client.entity.script.block

import smarthome.client.util.Position
import kotlin.reflect.typeOf

interface Block {
    val id: BlockId
    val position: Position
    fun copyWithPosition(newPosition: Position): Block
}