package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.dependency.Dependency

interface AddDependencyUseCase {
    fun execute(dependency: Dependency)
}