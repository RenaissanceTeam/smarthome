package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

interface GetSetupDependencyUseCase {
    fun execute(): DependencyDetails
}