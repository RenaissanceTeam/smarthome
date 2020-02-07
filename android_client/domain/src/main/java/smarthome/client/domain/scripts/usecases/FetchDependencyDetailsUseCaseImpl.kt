package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.DependencyRepo
import smarthome.client.domain.api.scripts.usecases.FetchDependencyDetailsUseCase
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

class FetchDependencyDetailsUseCaseImpl(
    private val repo: DependencyRepo
) : FetchDependencyDetailsUseCase {
    override suspend fun execute(scriptId: Long, dependencyId: DependencyId): DependencyDetails {
        return repo.getDetails(scriptId, dependencyId)
    }
}