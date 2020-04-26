package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.dependency.Dependency
interface GetDependencyUseCase {
    fun execute(dependencyId: String): Dependency
}