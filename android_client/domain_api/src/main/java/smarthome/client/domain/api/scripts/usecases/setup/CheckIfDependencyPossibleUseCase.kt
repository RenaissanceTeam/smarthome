package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.block.BlockId

interface CheckIfDependencyPossibleUseCase {
    fun execute(from: BlockId, to: BlockId): Boolean
}