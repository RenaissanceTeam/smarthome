package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.domain.api.scripts.usecases.GetScriptByIdUseCase
import smarthome.client.entity.script.Script

class GetScriptByIdUseCaseImpl(
    private val repo: ScriptsRepo
) : GetScriptByIdUseCase {
    override suspend fun execute(id: Long): Script {
        return repo.fetchOne(id)
    }
}