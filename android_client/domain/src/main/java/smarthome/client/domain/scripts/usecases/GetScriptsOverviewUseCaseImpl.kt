package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.domain.api.scripts.usecases.GetScriptsOverviewUseCase
import smarthome.client.entity.script.ScriptOverview

class GetScriptsOverviewUseCaseImpl(
        private val repo: ScriptsRepo
) : GetScriptsOverviewUseCase {
    override suspend fun execute(): List<ScriptOverview> {
        return repo.fetch()
    }
}