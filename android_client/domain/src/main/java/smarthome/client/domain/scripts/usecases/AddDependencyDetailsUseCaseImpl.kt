package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.DependencyDetailsRepo
import smarthome.client.domain.api.scripts.usecases.AddDependencyDetailsUseCase
import smarthome.client.entity.script.dependency.DependencyDetails

class AddDependencyDetailsUseCaseImpl(
    private val repo: DependencyDetailsRepo
) : AddDependencyDetailsUseCase {
    override fun execute(details: DependencyDetails) {
        repo.add(details)
    }
}