package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId
import smarthome.client.util.Position

interface MoveBlockUseCase {
    fun execute(scriptId: Long, blockId: BlockId, newPosition: Position): Block
}