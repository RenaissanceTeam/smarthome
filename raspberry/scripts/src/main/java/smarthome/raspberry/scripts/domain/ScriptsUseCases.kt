package smarthome.raspberry.scripts.domain
//
//import smarthome.library.common.scripts.Script
//import smarthome.raspberry.scripts.data.ScriptsRepository
//
//class ScriptsUseCases(private val repository: ScriptsRepository) {
//    suspend fun onNewScript(newScript: Script) {
//        repository.save(newScript)
//
//        runScriptActionsIfSatisfyConditions(newScript)
//    }
//
//    private suspend fun runScriptActionsIfSatisfyConditions(script: Script) {
//        if (script.conditions.all { it.satisfy() }) {
//            script.actions.forEach { it.run() }
//        }
//    }
//
//    suspend fun conditionsChanged() {
//        val scripts = repository.scripts
//
//        scripts.forEach { runScriptActionsIfSatisfyConditions(it) }
//    }
//
//    suspend fun deleteScript(script: Script) {
//        repository.delete(script)
//    }
//
//}