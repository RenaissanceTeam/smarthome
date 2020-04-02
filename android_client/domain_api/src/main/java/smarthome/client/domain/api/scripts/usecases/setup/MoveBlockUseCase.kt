package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position

interface MoveBlockUseCase {
    fun execute(blockId: String, newPosition: Position): Block
}