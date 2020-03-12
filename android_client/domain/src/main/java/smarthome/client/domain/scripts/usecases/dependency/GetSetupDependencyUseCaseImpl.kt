package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency

class GetSetupDependencyUseCaseImpl(
    private val setupDependencyRepo: SetupDependencyRepo
) : GetSetupDependencyUseCase {
    override fun execute(): Dependency {
        return setupDependencyRepo.get()
    }
}