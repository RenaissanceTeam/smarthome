package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.DependencyDetails

interface StartSetupDependencyUseCase {
    fun execute(scriptId: Long, dependencyDetails: DependencyDetails)
}