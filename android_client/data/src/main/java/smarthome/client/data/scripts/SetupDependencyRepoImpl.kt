package smarthome.client.data.scripts

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.entity.script.dependency.Dependency

class SetupDependencyRepoImpl : SetupDependencyRepo {
    private var dependency: Dependency? = null
    private val dependencyObservable = BehaviorSubject.create<Dependency>()
    private var scriptId: Long? = null
    
    override fun get(): Dependency {
        return dependency
            ?: throw IllegalStateException("Hasn't started the setup, so can't get dependency")
    }
    
    override fun set(dependency: Dependency) {
        this.dependency = dependency
        dependencyObservable.onNext(dependency)
    }
    
    override fun setScript(scriptId: Long) {
        this.scriptId = scriptId
    }
    
    override fun getScriptId(): Long {
        return scriptId
            ?: throw IllegalStateException("Hasn't started the setup, so can't get script id")
    }
    
    override fun observe(): Observable<Dependency> {
        return dependencyObservable
    }
}