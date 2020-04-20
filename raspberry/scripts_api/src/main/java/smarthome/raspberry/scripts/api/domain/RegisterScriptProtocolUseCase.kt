package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Script

interface RegisterScriptProtocolUseCase {
    fun execute(script: Script)

}