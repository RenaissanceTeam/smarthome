package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.AddConditionToSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase

class AddConditionToSetupDependencyUseCaseImpl(
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase,
    private val repo: SetupDependencyRepo,
    private val getEmptyConditionsForDependencyUseCase: CreateEmptyConditionsForDependencyUseCase
) : AddConditionToSetupDependencyUseCase {
    override fun execute() {
        val dependency = getSetupDependencyUseCase.execute()
    
        repo.set(dependency.copy(
            conditions = dependency.conditions + getEmptyConditionsForDependencyUseCase.execute(
                dependency = dependency
            ).first()
        ))
    }
}