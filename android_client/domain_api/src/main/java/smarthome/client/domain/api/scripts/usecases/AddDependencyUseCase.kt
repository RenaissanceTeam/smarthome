package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.Dependency

interface AddDependencyUseCase {
    fun execute(scriptId: Long, dependency: Dependency)
}