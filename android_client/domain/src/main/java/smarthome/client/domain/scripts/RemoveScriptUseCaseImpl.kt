package smarthome.client.domain.scripts

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.domain.api.scripts.RemoveScriptUseCase

class RemoveScriptUseCaseImpl(
        private val repo: ScriptsRepo
) : RemoveScriptUseCase {
    override suspend fun execute(id: Long) {
        repo.remove(id)
    }
}