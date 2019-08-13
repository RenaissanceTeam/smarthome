package smarthome.raspberry.domain

import smarthome.library.common.scripts.Script

interface ScriptsRepository {
    fun save(newScript: Script)
}