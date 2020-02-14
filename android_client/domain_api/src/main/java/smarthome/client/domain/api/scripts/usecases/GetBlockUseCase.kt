package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId

interface GetBlockUseCase {
    fun execute(scriptId: Long, id: BlockId): Block
}