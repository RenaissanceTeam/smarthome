package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.Condition

class CreateEmptyConditionsForBlockUseCaseImpl(
    private val graphRepo: ScriptGraphRepo,
    private val conditionFromBlock: ConditionFromBlockResolver
) : CreateEmptyConditionsForBlockUseCase {
    override fun execute(scriptId: Long, blockId: BlockId): List<Condition> {
        val block = graphRepo.getBlock(scriptId, blockId) ?: return emptyList()
        return conditionFromBlock.resolve(block)
    }
}

