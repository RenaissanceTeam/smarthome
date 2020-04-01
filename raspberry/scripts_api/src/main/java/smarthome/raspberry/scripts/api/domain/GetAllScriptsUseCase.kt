package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Script

interface GetAllScriptsUseCase {
    fun execute(): List<Script>
}