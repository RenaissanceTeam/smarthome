package smarthome.raspberry.scripts.api.domain.usecase

import smarthome.raspberry.entity.script.Script

interface RegisterScriptProtocolUseCase {
    fun execute(script: Script)
}