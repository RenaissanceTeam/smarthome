package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.Script

interface GetScriptByIdUseCase {
    suspend fun execute(id: Long): Script
}