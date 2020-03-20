package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.BlockId

interface GetBlockUseCase {
    fun execute(id: BlockId): Block
}