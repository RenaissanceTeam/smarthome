package smarthome.raspberry.domain.usecases

import smarthome.library.common.scripts.Script
import smarthome.raspberry.domain.ScriptsRepository

class ScriptsUseCases(private val repository: ScriptsRepository) {
    fun onNewScript(newScript: Script) {
        repository.save(newScript)
    }

}