package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.ScriptOverview

interface GetScriptsOverviewUseCase {
    suspend fun execute(): List<ScriptOverview>
}