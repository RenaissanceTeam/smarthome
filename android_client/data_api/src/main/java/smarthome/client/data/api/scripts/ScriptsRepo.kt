package smarthome.client.data.api.scripts

import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptOverview

interface ScriptsRepo {
    suspend fun fetch(): List<ScriptOverview>
    suspend fun fetchOne(id: Long): Script
    suspend fun save(script: Script): Script
    suspend fun remove(id: Long)
    suspend fun setEnabled(id: Long, isEnabled: Boolean)
}