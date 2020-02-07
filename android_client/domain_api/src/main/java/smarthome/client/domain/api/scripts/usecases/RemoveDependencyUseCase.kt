package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.DependencyId

interface RemoveDependencyUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId)
}