package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.data_api.HomeRepository
import smarthome.client.domain_api.NoScriptException
import smarthome.library.common.scripts.Script

class ScriptUseCase(private val repository: HomeRepository) {
    suspend fun getScripts(): Observable<MutableList<Script>> {
        return repository.getScripts()
    }

    suspend fun getScript(guid: Long): Script {
        val scripts = repository.getScripts().value
                ?: TODO("no value in scripts behavior subject")

        return scripts.find { it.guid == guid } ?: throw NoScriptException(guid)
    }

    suspend fun saveScript(script: Script) {
        repository.saveScript(script)
    }
}
