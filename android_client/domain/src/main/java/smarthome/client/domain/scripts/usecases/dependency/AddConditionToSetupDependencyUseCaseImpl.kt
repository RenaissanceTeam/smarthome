package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.AddConditionToSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase

class AddConditionToSetupDependencyUseCaseImpl(
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase,
    private val repo: SetupDependencyRepo,
    private val getEmptyConditionsForBlockUseCase: CreateEmptyConditionsForBlockUseCase
) : AddConditionToSetupDependencyUseCase {
    override fun execute() {
        
        val dependency = getSetupDependencyUseCase.execute()
        repo.set(dependency.copy(
            conditions = dependency.conditions + getEmptyConditionsForBlockUseCase.execute(
                scriptId = repo.getScriptId(),
                blockId = dependency.startBlock
            ).first()
        ))
    }
}