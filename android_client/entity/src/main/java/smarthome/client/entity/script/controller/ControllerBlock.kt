package smarthome.client.entity.script.controller

import smarthome.client.entity.script.block.Block

interface ControllerBlock : Block {
    val controllerId: Long
}