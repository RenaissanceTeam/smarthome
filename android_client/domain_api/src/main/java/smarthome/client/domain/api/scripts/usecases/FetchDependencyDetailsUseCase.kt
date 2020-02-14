package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

interface FetchDependencyDetailsUseCase {
    suspend fun execute(scriptId: Long, dependencyId: DependencyId): DependencyDetails
}