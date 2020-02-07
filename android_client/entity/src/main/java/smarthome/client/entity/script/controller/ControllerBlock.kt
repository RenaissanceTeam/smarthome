package smarthome.client.entity.script.controller

import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

//data class ControllerBlock(
//    val controllerId: Long,
//    override val position: Position
//) : Block {
//    override val id = ControllerBlockId(controllerId)
//
//    override fun copyWithPosition(newPosition: Position): Block {
//        return copy(position = newPosition)
//    }
//}

interface ControllerBlock : Block