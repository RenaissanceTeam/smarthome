package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Script

interface ScriptProtocol {
    fun register(script: Script)

}