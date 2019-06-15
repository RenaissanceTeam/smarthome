package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.library.common.scripts.Script

class ScriptUseCase(private val repository: HomeRepository) {
    fun getScripts(): Observable<MutableList<Script>> {
        TODO()
    }

    fun getScript(guid: Long): Script? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun saveScript(script: Script) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}