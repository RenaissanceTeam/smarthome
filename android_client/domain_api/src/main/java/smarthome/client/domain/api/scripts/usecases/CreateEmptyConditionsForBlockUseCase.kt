package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition

interface CreateEmptyConditionsForBlockUseCase {
    fun execute(scriptId: Long, blockId: BlockId): List<Condition>
}