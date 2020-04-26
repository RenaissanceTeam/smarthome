package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.ScriptInfo

interface UpdateScriptInfoUseCase {
    fun execute(info: ScriptInfo)
}