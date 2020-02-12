package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyDetails

class StartSetupDependencyUseCaseImpl(
    private val repo: SetupDependencyRepo
) : StartSetupDependencyUseCase {
    override fun execute(scriptId: Long, dependencyDetails: DependencyDetails) {
        repo.set(scriptId, dependencyDetails)
    }
}