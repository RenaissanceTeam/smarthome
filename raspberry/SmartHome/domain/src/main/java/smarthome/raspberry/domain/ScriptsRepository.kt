package smarthome.raspberry.domain

import smarthome.library.common.scripts.Script

interface ScriptsRepository {
    val scripts: List<Script>

    fun save(newScript: Script)
}