package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.Dependency

interface AddDependencyUseCase {
    fun execute(scriptId: Long, dependency: Dependency)
}