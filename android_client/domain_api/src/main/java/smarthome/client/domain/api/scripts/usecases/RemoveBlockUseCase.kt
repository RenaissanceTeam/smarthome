package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.BlockId

interface RemoveBlockUseCase {
    fun execute(scriptId: Long, blockId: BlockId)
}