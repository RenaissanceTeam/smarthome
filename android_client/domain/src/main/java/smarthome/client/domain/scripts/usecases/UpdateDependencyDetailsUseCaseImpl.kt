package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.DependencyDetailsRepo
import smarthome.client.domain.api.scripts.usecases.AddDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.GetDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.UpdateDependencyDetailsUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.DependencyCondition

class UpdateDependencyDetailsUseCaseImpl(
    private val getDependencyUseCase: GetDependencyUseCase,
    private val addDependencyDetailsUseCase: AddDependencyDetailsUseCase,
    private val repo: DependencyDetailsRepo
) : UpdateDependencyDetailsUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId, conditions: List<Condition>, action: Action) {
        val dependency = getDependencyUseCase.execute(scriptId, dependencyId)
        
        when (val details = repo.getByDependency(dependency)) {
            null -> addDependencyDetails(dependency, conditions, action)
            else -> repo.update(details)
        }
    }
    
    private fun addDependencyDetails(dependency: Dependency, conditions: List<Condition>,
                                     action: Action) {
        val condition = DependencyCondition(
            dependencyId = dependency.id,
            units = conditions
        )
        val details = DependencyDetails(dependency, condition, action)
        addDependencyDetailsUseCase.execute(details)
    }
}