package smarthome.raspberry.scripts.api.domain.usecase

import smarthome.raspberry.entity.script.Script

interface GetScriptByIdUseCase {
    fun execute(id: Long): Script
}