package smarthome.client.data.scripts

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

class SetupDependencyRepoImpl : SetupDependencyRepo {
    private var dependency: DependencyDetails? = null
    
    override fun get(scriptId: Long, dependencyId: DependencyId): DependencyDetails {
        return dependency
            ?: throw IllegalStateException("Hasn't started the setup, so can't get dependency")
    }
    
    override fun set(scriptId: Long, dependencyDetails: DependencyDetails) {
        this.dependency = dependencyDetails
    }
}