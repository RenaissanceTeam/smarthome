package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.Script

interface GetSetupScriptUseCase {
    fun execute(): Script?
}