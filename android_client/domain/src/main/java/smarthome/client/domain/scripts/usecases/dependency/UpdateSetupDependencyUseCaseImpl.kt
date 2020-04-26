package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.UpdateSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency

class UpdateSetupDependencyUseCaseImpl(
    private val setupDependencyRepo: SetupDependencyRepo
) : UpdateSetupDependencyUseCase {
    override fun execute(dependency: Dependency) {
        setupDependencyRepo.set(dependency)
    }
}