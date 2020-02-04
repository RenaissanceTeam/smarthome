package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.BlockId

interface RemoveBlockUseCase {
    fun execute(scriptId: Long, blockId: BlockId)
}