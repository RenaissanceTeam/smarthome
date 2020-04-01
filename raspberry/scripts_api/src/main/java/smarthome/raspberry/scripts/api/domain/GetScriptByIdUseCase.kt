package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Script

interface GetScriptByIdUseCase {
    fun execute(id: Long): Script
}