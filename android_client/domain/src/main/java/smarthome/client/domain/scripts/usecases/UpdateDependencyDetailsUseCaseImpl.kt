package smarthome.client.domain.scripts.usecases

import smarthome.client.domain.api.scripts.usecases.GetDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.UpdateDependencyDetailsUseCase
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

class UpdateDependencyDetailsUseCaseImpl(
    private val getDependencyUseCase: GetDependencyUseCase
    
) : UpdateDependencyDetailsUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId, conditions: List<Condition>, action: Action) {
//        val dependency = getDependencyUseCase.execute(scriptId, dependencyId)
//
//        when (val details = repo.getByDependency(dependency)) {
//            null -> addDependencyDetails(dependency, conditions, action)
//            else -> repo.save(details)
//        }
        TODO()
    }
    
//    private fun addDependencyDetails(dependency: Dependency,
//                                     conditions: List<Condition>,
//                                     action: Action) {
//
//        val details = DependencyDetails(dependency, conditions, action)
//        addDependencyDetailsUseCase.execute(details)
//    }
}