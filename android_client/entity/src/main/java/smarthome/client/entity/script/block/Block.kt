package smarthome.client.entity.script.block

import smarthome.client.util.Position
import kotlin.reflect.typeOf

interface Block {
    val id: BlockId
    val uuid: String
    val position: Position
    fun copyWithPosition(newPosition: Position): Block
}