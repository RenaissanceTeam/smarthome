package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.domain.api.scripts.usecases.FetchScriptsUseCase
import smarthome.client.entity.Script

class FetchScriptsUseCaseImpl(
    private val scriptsRepo: ScriptsRepo
) : FetchScriptsUseCase {
    override suspend fun execute(): List<Script> {
        return scriptsRepo.fetch()
    }
}