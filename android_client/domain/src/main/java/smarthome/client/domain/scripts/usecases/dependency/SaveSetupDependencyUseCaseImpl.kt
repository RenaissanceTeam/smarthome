package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.SaveSetupDependencyUseCase

class SaveSetupDependencyUseCaseImpl(
    private val repo: SetupDependencyRepo,
    private val dependencyRepo: SetupScriptRepo
) : SaveSetupDependencyUseCase {
    override fun execute() {
        dependencyRepo.updateDependency(repo.get())
    }
}