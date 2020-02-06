package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.DependencyId

interface RemoveDependencyUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId)
}