package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.Script

interface FetchScriptsUseCase {
    suspend fun execute(): List<Script>
}