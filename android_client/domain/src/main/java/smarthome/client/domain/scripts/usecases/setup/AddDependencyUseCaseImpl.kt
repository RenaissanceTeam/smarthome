package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.AddDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency

class AddDependencyUseCaseImpl(
    private val repo: SetupScriptRepo
) : AddDependencyUseCase {
    override fun execute(dependency: Dependency) {
        repo.addDependency(dependency)
    }
}