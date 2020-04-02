package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.RemoveDependencyUseCase
class RemoveDependencyUseCaseImpl(
    private val repo: SetupScriptRepo
) : RemoveDependencyUseCase {
    override fun execute(dependencyId: String) {
        repo.removeDependency(dependencyId)
    }
}