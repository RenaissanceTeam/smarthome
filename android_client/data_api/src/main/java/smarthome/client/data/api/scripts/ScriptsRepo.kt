package smarthome.client.data.api.scripts

import smarthome.client.entity.script.Script

interface ScriptsRepo {
    suspend fun fetch(): List<Script>
    suspend fun fetchOne(id: Long): Script
    suspend fun save(script: Script): Script
}