package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Script

interface AddScriptUseCase {
    fun execute(script: Script): Script
}