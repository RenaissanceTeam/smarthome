package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId

interface GetDependencyUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId): Dependency
}