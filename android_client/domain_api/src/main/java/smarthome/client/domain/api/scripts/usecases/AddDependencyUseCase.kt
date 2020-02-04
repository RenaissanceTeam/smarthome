package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Dependency

interface AddDependencyUseCase {
    fun execute(scriptId: Long, from: BlockId, to: BlockId, dependency: Dependency)
}