package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.BlockId

interface CheckIfDependencyPossibleUseCase {
    fun execute(scriptId: Long, from: BlockId, to: BlockId): Boolean
}