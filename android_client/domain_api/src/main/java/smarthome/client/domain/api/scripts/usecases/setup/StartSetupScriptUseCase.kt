package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.Script

interface StartSetupScriptUseCase {
    suspend fun execute(id: Long?): Script
}