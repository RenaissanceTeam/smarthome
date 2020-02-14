package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.BlockId

interface GetBlockNameUseCase {
    fun execute(scriptId: Long, blockId: BlockId): String
}