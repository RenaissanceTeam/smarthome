package smarthome.raspberry.scripts.api.domain.usecase

import smarthome.raspberry.entity.script.Script

interface GetAllScriptsUseCase {
    fun execute(): List<Script>
}