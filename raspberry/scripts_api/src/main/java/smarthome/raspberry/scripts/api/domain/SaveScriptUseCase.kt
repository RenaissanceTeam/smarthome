package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Script

interface SaveScriptUseCase {
    fun execute(script: Script): Script
}