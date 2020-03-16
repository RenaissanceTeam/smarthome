package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.action.Action

class CreateEmptyActionForDependencyUseCaseImpl(
    private val graphRepo: ScriptGraphRepo,
    private val actionFromBlock: ActionFromBlockResolver
) : CreateEmptyActionForDependencyUseCase {
    override fun execute(scriptId: Long, dependency: Dependency): List<Action> {
        val block = graphRepo.getBlock(scriptId, dependency.endBlock)
            ?: throw IllegalArgumentException("no block with id ${dependency.endBlock}")
        
        return actionFromBlock.resolve(block)
    }
}