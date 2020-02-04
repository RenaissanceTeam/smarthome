package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position

interface MoveBlockUseCase {
    fun execute(scriptId: Long, blockId: BlockId, newPosition: Position)
}