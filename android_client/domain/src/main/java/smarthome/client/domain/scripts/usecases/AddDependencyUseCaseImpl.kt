package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.AddDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.AddDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails

class AddDependencyUseCaseImpl(
    private val repo: ScriptGraphRepo,
    private val addDependencyDetailsUseCase: AddDependencyDetailsUseCase
) : AddDependencyUseCase {
    override fun execute(scriptId: Long, dependency: Dependency) {
        repo.addDependency(scriptId, dependency)
    
        addDependencyDetailsUseCase.execute(DependencyDetails(dependency))
    }
}