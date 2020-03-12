package smarthome.client.data.api.scripts

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.Dependency

interface SetupDependencyRepo {
    fun get(): Dependency
    fun set(dependency: Dependency)
    fun setScript(scriptId: Long)
    fun getScriptId(): Long
    fun observe(): Observable<Dependency>
}