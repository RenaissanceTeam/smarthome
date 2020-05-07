package smarthome.raspberry.scripts.api.domain.usecase

import smarthome.raspberry.entity.script.Script

interface SaveScriptUseCase {
    fun execute(script: Script): Script
}