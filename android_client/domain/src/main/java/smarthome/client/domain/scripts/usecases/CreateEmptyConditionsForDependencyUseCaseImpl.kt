package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.DependencyUnitId

class CreateEmptyConditionsForDependencyUseCaseImpl(
    private val graphRepo: ScriptGraphRepo,
    private val conditionFromBlock: ConditionFromBlockResolver
) : CreateEmptyConditionsForDependencyUseCase {
    override fun execute(scriptId: Long, dependency: Dependency): List<Condition> {
        val block = graphRepo.getBlock(scriptId, dependency.startBlock)
            ?: throw IllegalArgumentException("No block for id ${dependency.startBlock}")
        return conditionFromBlock.resolve(block)
    }
}

