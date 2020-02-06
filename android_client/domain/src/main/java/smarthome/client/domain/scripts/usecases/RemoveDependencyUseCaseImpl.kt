package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.RemoveDependencyUseCase
import smarthome.client.entity.script.DependencyId

class RemoveDependencyUseCaseImpl(
    private val repo: ScriptGraphRepo
) : RemoveDependencyUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId) {
        repo.removeDependency(scriptId, dependencyId)
    }
}