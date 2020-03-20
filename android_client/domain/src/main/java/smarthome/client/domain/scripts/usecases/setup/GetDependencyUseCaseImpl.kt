package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.GetDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId
import java.lang.IllegalArgumentException

class GetDependencyUseCaseImpl(
    private val repo: SetupScriptRepo
) : GetDependencyUseCase {
    override fun execute(dependencyId: DependencyId): Dependency {
        return repo.getDependency(dependencyId)
            ?: throw IllegalArgumentException("No such dependency with id $dependencyId")
    }
}