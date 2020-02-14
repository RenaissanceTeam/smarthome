package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.GetDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId
import java.lang.IllegalArgumentException

class GetDependencyUseCaseImpl(
    private val repo: ScriptGraphRepo
) : GetDependencyUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId): Dependency {
        return repo.getDependency(scriptId, dependencyId)
            ?: throw IllegalArgumentException("No such dependency with id $dependencyId")
    }
}