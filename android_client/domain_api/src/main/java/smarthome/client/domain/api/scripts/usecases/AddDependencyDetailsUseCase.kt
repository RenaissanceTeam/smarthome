package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.DependencyDetails

interface AddDependencyDetailsUseCase {
    fun execute(details: DependencyDetails)
}