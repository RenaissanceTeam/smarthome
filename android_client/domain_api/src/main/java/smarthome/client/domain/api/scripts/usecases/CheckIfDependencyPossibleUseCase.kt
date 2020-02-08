package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.BlockId

interface CheckIfDependencyPossibleUseCase {
    fun execute(scriptId: Long, from: BlockId, to: BlockId): Boolean
}