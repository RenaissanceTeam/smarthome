package smarthome.client.data.api.scripts

import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

interface SetupDependencyRepo {
    fun get(): DependencyDetails
    fun set(dependencyDetails: DependencyDetails)
    fun setScript(scriptId: Long)
    fun getScriptId(): Long
}