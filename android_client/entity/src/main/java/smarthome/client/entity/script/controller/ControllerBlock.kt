package smarthome.client.entity.script.controller

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position

interface ControllerBlock : Block {
    override val id: ControllerBlockId
}