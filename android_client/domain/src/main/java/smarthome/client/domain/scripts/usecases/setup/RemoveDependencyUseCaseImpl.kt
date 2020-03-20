package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.RemoveDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyId

class RemoveDependencyUseCaseImpl(
    private val repo: SetupScriptRepo
) : RemoveDependencyUseCase {
    override fun execute(dependencyId: DependencyId) {
        repo.removeDependency(dependencyId)
    }
}