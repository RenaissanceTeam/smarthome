package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId

interface GetDependencyUseCase {
    fun execute(dependencyId: DependencyId): Dependency
}