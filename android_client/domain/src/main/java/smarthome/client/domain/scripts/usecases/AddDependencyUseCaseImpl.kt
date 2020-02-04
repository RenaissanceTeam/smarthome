package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.AddDependencyUseCase
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Dependency

class AddDependencyUseCaseImpl(
    private val repo: ScriptGraphRepo
) : AddDependencyUseCase {
    
    override fun execute(scriptId: Long, from: BlockId, to: BlockId, dependency: Dependency) {
        TODO()
    }
}