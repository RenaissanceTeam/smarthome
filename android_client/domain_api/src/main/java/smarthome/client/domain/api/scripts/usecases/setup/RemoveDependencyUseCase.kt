package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.dependency.DependencyId

interface RemoveDependencyUseCase {
    fun execute(dependencyId: DependencyId)
}