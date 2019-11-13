package smarthome.raspberry.scripts.data

import smarthome.library.common.scripts.Script

interface ScriptsRepository {
    val scripts: List<Script>

    suspend fun save(newScript: Script)
    suspend fun delete(script: Script)
}