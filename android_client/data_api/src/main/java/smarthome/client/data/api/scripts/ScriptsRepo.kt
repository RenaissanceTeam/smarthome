package smarthome.client.data.api.scripts

import smarthome.client.entity.Script

interface ScriptsRepo {
    suspend fun fetch(): List<Script>
}