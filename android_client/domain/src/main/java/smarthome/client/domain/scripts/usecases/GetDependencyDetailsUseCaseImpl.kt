package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.DependencyDetailsRepo
import smarthome.client.domain.api.scripts.usecases.GetDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.GetDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

class GetDependencyDetailsUseCaseImpl(
    private val getDependencyUseCase: GetDependencyUseCase,
    private val repo: DependencyDetailsRepo
) : GetDependencyDetailsUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId): DependencyDetails {
        val dependency = getDependencyUseCase.execute(scriptId, dependencyId)
        return repo.getByDependency(dependency)
            ?: throw IllegalArgumentException("No details for id $dependencyId")
    }
}