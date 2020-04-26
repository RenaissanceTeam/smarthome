package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.condition.Condition

class CreateEmptyConditionsForDependencyUseCaseImpl(
    private val graphRepo: SetupScriptRepo,
    private val conditionFromBlock: ConditionFromBlockResolver
) : CreateEmptyConditionsForDependencyUseCase {
    override fun execute(dependency: Dependency): List<Condition> {
        val block = graphRepo.getBlock(dependency.startBlock)
            ?: throw IllegalArgumentException("No block for id ${dependency.startBlock}")
        return conditionFromBlock.resolve(block)
    }
}

