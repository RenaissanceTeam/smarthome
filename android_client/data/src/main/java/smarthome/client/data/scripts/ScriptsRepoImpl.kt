package smarthome.client.data.scripts

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.data.scripts.mapper.ScriptDtoToScriptMapper
import smarthome.client.entity.script.Script

class ScriptsRepoImpl(
    private val retrofitFactory: RetrofitFactory,
    private val scriptsMapper: ScriptDtoToScriptMapper
) : ScriptsRepo {
    override suspend fun fetch(): List<Script> {
        // todo remove mock when endpoint is ready
        return (1..20).map {
            Script(it.toLong(), "script # $it")
        }
        
        val scriptsDtos = retrofitFactory.createApi(ScriptsApi::class.java)
            .fetchAll()
        
        return scriptsDtos.map(scriptsMapper::map)
    }
}