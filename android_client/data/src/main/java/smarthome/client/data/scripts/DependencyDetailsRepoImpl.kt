package smarthome.client.data.scripts

import smarthome.client.data.api.scripts.DependencyDetailsRepo
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails

class DependencyDetailsRepoImpl : DependencyDetailsRepo {
    private val details = mutableMapOf<Dependency, DependencyDetails>()
    
    override fun getByDependency(dependency: Dependency): DependencyDetails? {
        return details[dependency]
    }
    
    override fun save(details: DependencyDetails) {
        this.details[details.dependency] = details
    }
}