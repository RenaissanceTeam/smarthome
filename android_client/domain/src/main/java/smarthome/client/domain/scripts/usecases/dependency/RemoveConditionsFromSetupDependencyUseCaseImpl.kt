package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.RemoveConditionsFromSetupDependencyUseCase
import smarthome.client.entity.script.dependency.condition.DependencyUnitId

class RemoveConditionsFromSetupDependencyUseCaseImpl(
    private val getSetupDependencyUseCase: GetSetupDependencyUseCase,
    private val repo: SetupDependencyRepo
) : RemoveConditionsFromSetupDependencyUseCase {
    
    override fun execute(vararg ids: DependencyUnitId) {
        val dependency = getSetupDependencyUseCase.execute()
        
        repo.set(dependency.copy(
            conditions = dependency.conditions.filterNot { it.id in ids }
        ))
    }
}