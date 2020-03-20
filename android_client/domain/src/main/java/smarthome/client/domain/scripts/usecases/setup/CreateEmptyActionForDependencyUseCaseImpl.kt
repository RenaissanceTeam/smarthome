package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyActionForDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.action.Action

class CreateEmptyActionForDependencyUseCaseImpl(
    private val graphRepo: SetupScriptRepo,
    private val actionFromBlock: ActionFromBlockResolver
) : CreateEmptyActionForDependencyUseCase {
    override fun execute(dependency: Dependency): List<Action> {
        val block = graphRepo.getBlock(dependency.endBlock)
            ?: throw IllegalArgumentException("no block with id ${dependency.endBlock}")
        
        return actionFromBlock.resolve(block)
    }
}