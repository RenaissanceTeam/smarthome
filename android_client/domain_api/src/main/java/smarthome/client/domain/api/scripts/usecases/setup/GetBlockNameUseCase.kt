package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.BlockId

interface GetBlockNameUseCase {
    fun execute(blockId: BlockId): String
}