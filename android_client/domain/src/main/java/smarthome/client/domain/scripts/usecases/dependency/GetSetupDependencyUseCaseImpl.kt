package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

class GetSetupDependencyUseCaseImpl(
    private val setupDependencyRepo: SetupDependencyRepo
) : GetSetupDependencyUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId): DependencyDetails {
        return setupDependencyRepo.get(scriptId, dependencyId)
    }
}