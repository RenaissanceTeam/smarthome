package smarthome.raspberry.domain.usecases

import smarthome.library.common.scripts.Script
import smarthome.raspberry.domain.ScriptsRepository

class ScriptsUseCases(private val repository: ScriptsRepository) {
    suspend fun onNewScript(newScript: Script) {
        repository.save(newScript)

        runScriptActionsIfSatisfyConditions(newScript)
    }

    private suspend fun runScriptActionsIfSatisfyConditions(script: Script) {
        if (script.conditions.all { it.satisfy() }) {
            script.actions.forEach { it.run() }
        }
    }

    suspend fun conditionsChanged() {
        val scripts = repository.scripts

        scripts.forEach { runScriptActionsIfSatisfyConditions(it) }
    }

}