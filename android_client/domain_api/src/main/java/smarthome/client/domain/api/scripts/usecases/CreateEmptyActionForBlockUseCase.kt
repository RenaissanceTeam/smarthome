package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action

interface CreateEmptyActionForBlockUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId, blockId: BlockId): Action
}