package smarthome.client.data.scripts

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.log

class SetupDependencyRepoImpl : SetupDependencyRepo {
    private var dependency: DependencyDetails? = null
    private val dependencyObservable = BehaviorSubject.create<DependencyDetails>()
    private var scriptId: Long? = null
    
    override fun get(): DependencyDetails {
        return dependency
            ?: throw IllegalStateException("Hasn't started the setup, so can't get dependency")
    }
    
    override fun set(dependencyDetails: DependencyDetails) {
        this.dependency = dependencyDetails
        dependencyObservable.onNext(dependencyDetails)
    }
    
    override fun setScript(scriptId: Long) {
        this.scriptId = scriptId
    }
    
    override fun getScriptId(): Long {
        return scriptId
            ?: throw IllegalStateException("Hasn't started the setup, so can't get script id")
    }
    
    override fun observe(): Observable<DependencyDetails> {
        return dependencyObservable
    }
}