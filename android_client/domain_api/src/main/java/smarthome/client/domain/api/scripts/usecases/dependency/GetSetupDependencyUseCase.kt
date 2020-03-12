package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.Dependency

interface GetSetupDependencyUseCase {
    fun execute(): Dependency
}