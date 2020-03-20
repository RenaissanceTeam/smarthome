package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.domain.api.scripts.usecases.setup.FetchScriptsUseCase
import smarthome.client.entity.script.Script

class FetchScriptsUseCaseImpl(
    private val scriptsRepo: ScriptsRepo
) : FetchScriptsUseCase {
    override suspend fun execute(): List<Script> {
        return scriptsRepo.fetch()
    }
}