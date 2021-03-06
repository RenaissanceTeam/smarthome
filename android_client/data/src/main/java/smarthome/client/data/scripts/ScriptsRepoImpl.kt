package smarthome.client.data.scripts

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.entity.script.Script
import smarthome.client.entity.script.ScriptOverview

class ScriptsRepoImpl(
        private val retrofitFactory: RetrofitFactory
) : ScriptsRepo {

    override suspend fun fetch(): List<ScriptOverview> {
        return retrofitFactory.createApi(ScriptsApi::class.java)
                .fetchAll()
    }

    override suspend fun fetchOne(id: Long): Script {
        return retrofitFactory.createApi(ScriptsApi::class.java)
                .fetchDetails(id)
    }

    override suspend fun setEnabled(id: Long, isEnabled: Boolean) {
        retrofitFactory.createApi(ScriptsApi::class.java)
                .setEnabled(id, isEnabled)
    }

    override suspend fun save(script: Script): Script {
        return retrofitFactory.createApi(ScriptsApi::class.java)
                .save(script)
    }

    override suspend fun remove(id: Long) {
        retrofitFactory.createApi(ScriptsApi::class.java)
                .remove(id)
    }
}