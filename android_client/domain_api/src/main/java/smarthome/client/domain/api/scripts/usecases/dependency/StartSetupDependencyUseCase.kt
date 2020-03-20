package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId

interface StartSetupDependencyUseCase {
    fun execute(dependencyId: DependencyId): Dependency
}